package com.busra.connecting.model.serdes;

import com.busra.connecting.model.HistoryCommand;
import org.springframework.kafka.support.serializer.JsonSerde;

public class HistoryCommandSerde extends JsonSerde<HistoryCommand> {
    public HistoryCommandSerde(){
        super();
        this.ignoreTypeHeaders();
    }
}
