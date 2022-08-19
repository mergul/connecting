package com.busra.connecting.model.serdes;

import com.busra.connecting.model.PaymentCommand;
import org.springframework.kafka.support.serializer.JsonSerde;

public class PaymentCommandSerde extends JsonSerde<PaymentCommand> {
    public PaymentCommandSerde(){
        super();
        this.ignoreTypeHeaders();
    }
}
