package com.busra.connecting.service;

import com.busra.connecting.config.streams.KStreamConf;
import com.busra.connecting.model.News;
import com.busra.connecting.model.NewsPayload;
import com.google.common.util.concurrent.*;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class NewsService {

    private static final String NEWS_STORE = "connect-news-stores";
   // private static final String NEWS_USER_STORE = "connect-news-user-stores";
    final ListeningExecutorService pool = MoreExecutors.listeningDecorator(
            Executors.newFixedThreadPool(8)
    );
    private final StreamsBuilderFactoryBean factoryBean;

    public NewsService(StreamsBuilderFactoryBean factoryBean) {
        this.factoryBean = factoryBean;
    }

    //@SneakyThrows
    public Mono<News> findById(String newsId) {
//        logger.info("Number of sub topologies => {}", this.factoryBean.getTopology().describe());
        final ListenableFuture<ReadOnlyKeyValueStore<byte[], News>> newsFuture = future(NEWS_STORE);
        Mono<ReadOnlyKeyValueStore<byte[], News>> newsStore = Mono.fromFuture(toCompletableFuture(newsFuture));
        return newsStore.flatMap(store -> Mono.fromCallable(()->store.get(newsId.getBytes())).subscribeOn(Schedulers.boundedElastic())).onErrorReturn(News.of().build());
    }

   // @SneakyThrows
    public Flux<NewsPayload> findAllByTopicsPart(String part) {
        final ListenableFuture<ReadOnlyKeyValueStore<byte[], News>> newsFuture = future(NEWS_STORE);
        Mono<ReadOnlyKeyValueStore<byte[], News>> newsStore = Mono.fromFuture(toCompletableFuture(newsFuture));
        List<NewsPayload> list = new ArrayList<NewsPayload>();
       return newsStore.map(ReadOnlyKeyValueStore::all).flatMapIterable(iterator -> {
           iterator.forEachRemaining(userKeyValue -> {
               if (userKeyValue.value.getTopic().contains(part)) {
                    list.add(getNewsPayload(userKeyValue.value));
                }
           });
            iterator.close();
            return list;
        });
    }

    private NewsPayload getNewsPayload(News value) {
        return NewsPayload.of(value.getId())
                .withThumb(value.getMediaReviews().get(0).getFile_name())
                .withClean(value.getClean())
                .withNewsOwner(value.getOwner())
                .withNewsOwnerId(value.getOwnerId())
                .withTags(value.getTags())
                .withTopic(value.getTopic())
                .withCount(value.getCount())
                .withTopics(value.getTags())
                .withDate(value.getDate())
                .withOwnerUrl(value.getOwnerUrl())
                .build();
    }
    public static  <K, V> CompletableFuture<ReadOnlyKeyValueStore<K, V>> toCompletableFuture(ListenableFuture<ReadOnlyKeyValueStore<K, V>> listenableFuture) {
        final CompletableFuture<ReadOnlyKeyValueStore<K, V>> completableFuture = new CompletableFuture<ReadOnlyKeyValueStore<K, V>>();
        Futures.addCallback(listenableFuture, new FutureCallback<ReadOnlyKeyValueStore<K, V>>() {
            @Override
            public void onSuccess(ReadOnlyKeyValueStore<K, V> result) {
                completableFuture.complete(result);
            }

            @Override
            @ParametersAreNonnullByDefault
            public void onFailure(Throwable t) {
                completableFuture.completeExceptionally(t);
            }
        }, MoreExecutors.directExecutor());

        return completableFuture;
    }
    public static <T> T waitUntilStoreIsQueryable(final String storeName,
                                                  final QueryableStoreType<T> queryableStoreType,
                                                  final KafkaStreams streams) throws InterruptedException {
        KStreamConf.startupLatch.await();
        return streams.store(StoreQueryParameters.fromNameAndType(storeName, queryableStoreType));
    }
    public <K, V> ListenableFuture<ReadOnlyKeyValueStore<K, V>> future(final String storeName) {
        return pool.submit(() -> waitUntilStoreIsQueryable(storeName, QueryableStoreTypes.keyValueStore(), this.factoryBean.getKafkaStreams()));
    }
}
