package com.busra.connecting.model.serdes;

import com.busra.connecting.model.News;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class NewsSerdes implements Serializer<News>, Deserializer<News> {
    private static final Logger log = LoggerFactory.getLogger(NewsSerdes.class.getName());

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, News news) {
        try {
            return objectMapper.writeValueAsBytes(news);
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize object {}", news, e);
            return null;
        }
    }

    @Override
    public News deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(new String(data, "UTF-8"), News.class);
        } catch (Exception e) {
            log.error("Unable to deserialize message {}", data, e);
            return null;
        }
    }

    @Override
    public void close() {
    }
}
