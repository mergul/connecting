package com.busra.connecting.service;

//@Service
public class StoreService {

//    private final StreamsBuilderFactoryBean builder;
//
//    public StoreService(StreamsBuilderFactoryBean builder) {
//        this.builder = builder;
//    }
//
//    public <K, V> CompletableFuture<ReadOnlyKeyValueStore<K, V>> getStore(String storeName) {
//        Callable<ReadOnlyKeyValueStore<K, V>> callable = () -> waitUntilStoreIsQueryable(storeName, QueryableStoreTypes.keyValueStore(), this.builder.getKafkaStreams());
//        ListenableFutureTask<ReadOnlyKeyValueStore<K, V>> task = new ListenableFutureTask<>(callable);
//        return task.completable();
//    }
//    public static <T> T waitUntilStoreIsQueryable(final String storeName,
//                                                  final QueryableStoreType<T> queryableStoreType,
//                                                  final KafkaStreams streams) throws InterruptedException {
//        KStreamConf.startupLatch.await();
//        return streams.store(StoreQueryParameters.fromNameAndType(storeName, queryableStoreType));
//    }
}
