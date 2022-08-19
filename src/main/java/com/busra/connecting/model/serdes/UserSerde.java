package com.busra.connecting.model.serdes;

import com.busra.connecting.model.User;
import org.springframework.kafka.support.serializer.JsonSerde;

public class UserSerde extends JsonSerde<User> {
    public UserSerde(){
        super();
        this.ignoreTypeHeaders();
    }
}
