package com.busra.connecting.model.serdes;

import com.busra.connecting.model.CommentsFeed;
import org.springframework.kafka.support.serializer.JsonSerde;

public class CommentsFeedSerde extends JsonSerde<CommentsFeed> {
    public CommentsFeedSerde(){
        super();
        this.ignoreTypeHeaders();
    }
}
