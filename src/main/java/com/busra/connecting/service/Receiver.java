package com.busra.connecting.service;

//@Service
public class Receiver {

//  private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
//  private final ObjectMapper mapper=new ObjectMapper();
//  private CountDownLatch latch = new CountDownLatch(2);
//
//  public CountDownLatch getLatch() {
//    return latch;
//  }

//  @KafkaListener(topics = "${kafka.topics.bar}")
//  public void receiveBar(NewsPayload bar) throws IOException {
//    LOGGER.info("received {}", bar);
////    LOGGER.info("received mapper {}", mapper.readValue((byte[]) cr.value(), Bar.class));
////    LOGGER.info(cr.toString());
//    latch.countDown();
//  }

//  @KafkaListener(topics = "${topics.kafka.auths-out}", properties = { "spring.json.value.default.type=com.example.dero.model.UserPayload", "spring.json.use.type.headers=false" })
//  public void receiveFoo(UserPayload foo) throws IOException {
//    LOGGER.info("received {}", foo);
////    LOGGER.info(cr.toString()); ConsumerRecord<?, ?> cr,
////    LOGGER.info("received mapper {}",mapper.readValue((byte[]) cr.value(), UserPayload.class));
//    latch.countDown();
//  }

//  @Bean
//  public RecordMessageConverter converter() {
//    ByteArrayJsonMessageConverter converter = new ByteArrayJsonMessageConverter();
//    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
//    typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
//    typeMapper.addTrustedPackages("com.example.demo.model");
//    Map<String, Class<?>> mappings = new HashMap<>();
//    mappings.put("user", UserPayload.class);
//    mappings.put("news", NewsPayload.class);
//    typeMapper.setIdClassMapping(mappings);
//    converter.setTypeMapper(typeMapper);
//    return converter;
//  }
}
