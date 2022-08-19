package com.busra.connecting.model.serdes;

import com.busra.connecting.model.ListCommand;
import org.springframework.kafka.support.serializer.JsonSerde;

public class ListCommandSerde extends JsonSerde<ListCommand> {
    public ListCommandSerde(){
        super();
        this.ignoreTypeHeaders();
    }
}
