package com.busra.connecting.model;

import reactor.core.publisher.Mono;

public abstract class AbstractKafkaSender {
    public abstract <T> Mono<Boolean> sendMessage(T message, Class<T> returnType);
}
