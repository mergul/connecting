package com.busra.connecting.model.serdes;

import com.busra.connecting.model.BalanceCommand;
import org.springframework.kafka.support.serializer.JsonSerde;

public class BalanceCommandSerde extends JsonSerde<BalanceCommand> {
    public BalanceCommandSerde(){
        super();
        this.ignoreTypeHeaders();
    }
}
