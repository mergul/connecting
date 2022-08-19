package com.busra.connecting.model.serdes;

import com.busra.connecting.model.News;
import org.springframework.kafka.support.serializer.JsonSerde;

public class NewsSerde extends JsonSerde<News> {
    public NewsSerde(){
        super();
        this.ignoreTypeHeaders();
    }
}
