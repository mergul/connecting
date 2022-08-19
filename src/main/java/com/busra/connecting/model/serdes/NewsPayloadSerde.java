package com.busra.connecting.model.serdes;

import com.busra.connecting.model.NewsPayload;
import org.springframework.kafka.support.serializer.JsonSerde;

public class NewsPayloadSerde extends JsonSerde<NewsPayload> {
    public NewsPayloadSerde(){
        super();
        this.ignoreTypeHeaders();
    }
}
