package com.busra.connecting.service;

import com.busra.connecting.model.ObjectId;
import com.busra.connecting.model.User;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

@Service
public class UserService implements ReactiveUserDetailsService {

    private static final String USERS_STORES = "connect-users-stores";
    private static final String USERNAME_STORE = "connect-username-stores";
    final ListeningExecutorService pool = MoreExecutors.listeningDecorator(
            Executors.newFixedThreadPool(8)
    );
    private final StreamsBuilderFactoryBean factoryBean;
    private final Sender kafkaSender;

    public UserService(StreamsBuilderFactoryBean factoryBean, Sender kafkaSender) {
        this.factoryBean = factoryBean;
        this.kafkaSender = kafkaSender;
    }

 //   @SneakyThrows
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        final ListenableFuture<ReadOnlyKeyValueStore<byte[], User>> usersFuture = future(USERS_STORES);
        Mono<ReadOnlyKeyValueStore<byte[], User>> usersStore = Mono.fromFuture(NewsService.toCompletableFuture(usersFuture));
        return usersStore.flatMap(store -> Mono.fromCallable(()->store.get(username.getBytes())).subscribeOn(Schedulers.boundedElastic()));

    }

    public Mono<User> save(User user) {
        return this.kafkaSender.send(NewsStreams.USERS_OUT, user, user.getId().toHexString().getBytes(),true).subscribeOn(Schedulers.boundedElastic()).map(aBoolean -> user);
    }

   // @SneakyThrows
    public Mono<User> findById(String id) {
        final ListenableFuture<ReadOnlyKeyValueStore<byte[], User>> usersFuture = future(USERS_STORES);
        Mono<ReadOnlyKeyValueStore<byte[], User>> usersStore = Mono.fromFuture(NewsService.toCompletableFuture(usersFuture));
        return usersStore.flatMap(store -> Mono.fromCallable(()->store.get(id.length()==12?new ObjectId(id.getBytes()).toHexString().getBytes():id.getBytes()))
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.just(User.of(new ObjectId(id)).withRoles(Collections.emptyList()).build())));
    }

  //  @SneakyThrows
    public Flux<User> findAllByIds(List<String> ids) {
        final ListenableFuture<ReadOnlyKeyValueStore<byte[], User>> usersFuture = future(USERS_STORES);
        Mono<ReadOnlyKeyValueStore<byte[], User>> usersStore = Mono.fromFuture(NewsService.toCompletableFuture(usersFuture));
        return usersStore.flatMapMany(store -> Flux.fromIterable(ids).publishOn(Schedulers.boundedElastic())
                .map(sid -> store.get(sid.getBytes())));
    }
  //  @SneakyThrows
    public Flux<User> findAllByUsernamePart(String part) {
        final ListenableFuture<ReadOnlyKeyValueStore<byte[], User>> usersFuture = future(USERS_STORES);
        Mono<ReadOnlyKeyValueStore<byte[], User>> usersStore = Mono.fromFuture(NewsService.toCompletableFuture(usersFuture));
        List<User> list = new ArrayList<User>();
        return usersStore.map(ReadOnlyKeyValueStore::all).flatMapIterable(iterator -> {
            iterator.forEachRemaining(userKeyValue -> {
                if(userKeyValue.value.getUsername().contains(part)) list.add(userKeyValue.value);
            });
            iterator.close();
            return list;
        });
    }

   // @SneakyThrows
    public Mono<User> findByUsernameId(String username) {
        final ListenableFuture<ReadOnlyKeyValueStore<byte[], byte[]>> usersFuture = future(USERNAME_STORE);
        Mono<ReadOnlyKeyValueStore<byte[], byte[]>> userNameStore = Mono.fromFuture(NewsService.toCompletableFuture(usersFuture));
        return userNameStore.flatMap(store -> Mono.fromCallable(()->store.get(username.getBytes())).subscribeOn(Schedulers.boundedElastic()))
                .flatMap(bytes -> this.findById(new String(bytes)));
    }

    public <K, V> ListenableFuture<ReadOnlyKeyValueStore<K, V>> future(final String storeName) {
        return pool.submit(() -> NewsService.waitUntilStoreIsQueryable(storeName, QueryableStoreTypes.keyValueStore(), this.factoryBean.getKafkaStreams()));
    }
}
