package com.busra.connecting.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = RecordSSE.Builder.class)
public class RecordSSE{
    private String key;
    private Long value;
    public  RecordSSE(){}

    @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
    public RecordSSE(String key,Long value){
        this.key=key;
        this.value=value;
    }
    public static Builder of() {
        return new Builder();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RecordSSE{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static final class Builder {

        private String key;
        private Long value;

        public Builder(){}

        public Builder(String key){
            this.key=key;
        }

        public Builder withKey(String key){
            this.key =key;
            return this;
        }

        public Builder withValue(Long value){
            this.value =value;
            return this;
        }

        public RecordSSE build(){
            return new RecordSSE(key,value);
        }
    }
}
