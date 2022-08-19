package com.busra.connecting.model.serdes;

import com.busra.connecting.model.CheckoutCommand;
import org.springframework.kafka.support.serializer.JsonSerde;

public class CheckoutCommandSerde extends JsonSerde<CheckoutCommand> {
    public CheckoutCommandSerde(){
        super();
        this.ignoreTypeHeaders();
    }
}
