package com.busra.connecting.model.serdes;

import com.busra.connecting.model.RecordSSE;
import org.springframework.kafka.support.serializer.JsonSerde;

public class RecordSSESerde extends JsonSerde<RecordSSE> {
    public RecordSSESerde(){
        super();
        this.ignoreTypeHeaders();
    }
}
