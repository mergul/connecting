package com.busra.connecting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private KafkaTemplate<byte[], Object> kafkaTemplate;

    public Mono<Boolean> send(String topic, Object payload, byte[] key, Boolean istream) {
        LOGGER.info("sending payload='{}' to topic='{}' with key --> '{}'", payload, topic, new String(key));
        if (istream) {
            return Mono.fromFuture(kafkaTemplate.send(topic, key, payload).completable()).thenReturn(true);
        } else
            return Mono.fromFuture(kafkaTemplate.send(MessageBuilder.withPayload(payload).setHeader(KafkaHeaders.TOPIC, topic)
                    .build()).completable()).thenReturn(true);

    }
}
// .setHeader("__TypeId__", clazz.getName())
