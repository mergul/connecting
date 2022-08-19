package com.busra.connecting.config.consumers;

//@Configuration
//@EnableKafka
public class ReceiverConfig {

//  @Value("${spring.kafka.bootstrap-servers}")
//  private String bootstrapServers;
//
//  @Bean
//  public Map<String, Object> consumerConfigs() {
//    Map<String, Object> props = new HashMap<>();
//    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
//    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//    props.put(ConsumerConfig.GROUP_ID_CONFIG, "json");
//    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//
//    return props;
//  }
//
//  @Bean
//  public ConsumerFactory<byte[], Object> consumerFactory() {
//    return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new ByteArrayDeserializer(),
//        new JsonDeserializer<>().trustedPackages("*"));
//  }
//
//  @Bean
//  public ConcurrentKafkaListenerContainerFactory<byte[], Object> kafkaListenerContainerFactory() {
//    ConcurrentKafkaListenerContainerFactory<byte[], Object> factory =
//        new ConcurrentKafkaListenerContainerFactory<>();
//    factory.setConsumerFactory(consumerFactory());
//    factory.setMessageConverter(new ByteArrayJsonMessageConverter());
//
//    return factory;
//  }

}
