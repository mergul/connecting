package com.busra.connecting.config.streams;

import com.busra.connecting.model.News;
import com.busra.connecting.model.User;
import com.busra.connecting.model.serdes.NewsSerde;
import com.busra.connecting.model.serdes.UserSerde;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanCustomizer;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KStreamConf {
    private static final Logger logger = LoggerFactory.getLogger(KStreamConf.class);
//    final AtomicBoolean builderConfigured = new AtomicBoolean();
//    final AtomicBoolean topologyConfigured = new AtomicBoolean();
    private static final String USERS_STORES = "connect-users-stores";
    private static final String USERNAME_STORE = "connect-username-stores";
//    private static final String COUNTS_STORE = "connect-counts-store";
    private static final String NEWS_STORE = "connect-news-stores";
//    private static final String NEWS_USER_STORE = "connect-news-user-stores";
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${topics.kafka.users-in}")
    private String usersTopics;
    @Value("${topics.kafka.news-in}")
    private String newsTopics;
//    @Value("${topics.kafka.pageviews-in}")
//    private String pageviewsTopics;
    public final static CountDownLatch startupLatch = new CountDownLatch(1);

    @Bean
    public RecordMessageConverter converter() {
        return new ByteArrayJsonMessageConverter();
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "connectStreamName");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        props.put(StreamsConfig.STATE_DIR_CONFIG, "data");
//        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        props.put(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, 1);
        props.put(StreamsConfig.producerPrefix(ProducerConfig.METADATA_MAX_AGE_CONFIG), 500);
        props.put(StreamsConfig.consumerPrefix(ConsumerConfig.METADATA_MAX_AGE_CONFIG), 500);
        props.put(StreamsConfig.consumerPrefix(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG), true);
        props.put(StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG), "latest");
        props.put(StreamsConfig.consumerPrefix(ConsumerConfig.GROUP_ID_CONFIG), "share-group");
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, JsonNode.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public StreamsBuilderFactoryBeanConfigurer configurer() {
        return fb -> fb.setStateListener((newState, oldState) -> {
            if (newState == KafkaStreams.State.RUNNING && oldState == KafkaStreams.State.REBALANCING) {
                startupLatch.countDown();
            } else if (newState != KafkaStreams.State.RUNNING) {
                logger.info("State is => Not Ready");
            }
            logger.info("State transition from " + oldState + " to " + newState);
        });
    }

    @Bean
    public KStream<byte[], User> kStream(StreamsBuilder builder) {
        KStream<byte[], User> usersInput = builder.stream(usersTopics, Consumed.with(new Serdes.ByteArraySerde(), new UserSerde()));
        KStream<byte[], News> newsInput = builder.stream(newsTopics, Consumed.with(new Serdes.ByteArraySerde(), new NewsSerde()));

        KTable<byte[], User> fgh = usersInput.groupByKey().reduce((value1, value2) -> value2
                , Materialized.<byte[], User, KeyValueStore<Bytes, byte[]>>as(USERS_STORES)
                        .withKeySerde(Serdes.ByteArray())
                        .withValueSerde(new UserSerde()));
        fgh.toStream().map((key, value) -> KeyValue.pair(value.getUsername().getBytes(), key)).groupByKey()
                .reduce((value1, value2) -> value2
                        , Materialized.<byte[], byte[], KeyValueStore<Bytes, byte[]>>as(USERNAME_STORE)
                                .withKeySerde(Serdes.ByteArray())
                                .withValueSerde(Serdes.ByteArray()));

        newsInput.groupByKey().reduce((value1, value2) -> value2, Materialized.<byte[], News, KeyValueStore<Bytes, byte[]>>as(NEWS_STORE)
                .withKeySerde(Serdes.ByteArray())
                .withValueSerde(new NewsSerde()));
        return usersInput;
    }

}
//    @Bean
//    public RecordMessageConverter converter() {
//        ByteArrayJsonMessageConverter converter = new ByteArrayJsonMessageConverter();
//        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
//        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
//        typeMapper.addTrustedPackages("com.example.demo.model");
//        Map<String, Class<?>> mappings = new HashMap<>();
//        mappings.put("user", UserPayload.class);
//        mappings.put("news", NewsPayload.class);
//        typeMapper.setIdClassMapping(mappings);
//        converter.setTypeMapper(typeMapper);
//        return converter;
//    }

// KStream<byte[], RecordSSE> viewsIn = builder.stream(pageviewsTopics, Consumed.with(new Serdes.ByteArraySerde(), new RecordSSESerde()));
//        viewsIn.mapValues((rok, value) -> {
//            ChatRoomEntry<RecordSSE> chatRoomEntry = newsHistoryService.getChatRoom("TopNews");
//            if (!value.getKey().equals("@total")) {
//                chatRoomEntry.addMsg(value, value.getKey(), new Date(), "news-counts");
//            }
//            return value;
//        }).filter((key, value) -> value.getKey().charAt(0) == '@').groupByKey().reduce((value1, value2) -> value2,
//                Materialized.<byte[], RecordSSE, KeyValueStore<Bytes, byte[]>>as(COUNTS_STORE)
//                        .withKeySerde(Serdes.ByteArray())
//                        .withValueSerde(new RecordSSESerde()));
//

//        hjk.toStream().map((key, value) -> KeyValue.pair(value.getOwnerId().getBytes(), key)).groupByKey()
//                .aggregate(ByteDataAccu::new, (key, value, aggregate) -> aggregate.add(value)
//                        , Materialized.<byte[], ByteDataAccu, KeyValueStore<Bytes, byte[]>>as(NEWS_USER_STORE)
//                                .withKeySerde(Serdes.ByteArray())
//                                .withValueSerde(Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(ByteDataAccu.class))));
