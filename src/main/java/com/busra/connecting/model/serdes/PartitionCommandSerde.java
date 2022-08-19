package com.busra.connecting.model.serdes;

import com.busra.connecting.model.PartitionCommand;
import org.springframework.kafka.support.serializer.JsonSerde;

public class PartitionCommandSerde extends JsonSerde<PartitionCommand> {
    public PartitionCommandSerde(){
        super();
        this.ignoreTypeHeaders();
    }
}
