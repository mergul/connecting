package com.busra.connecting.config.streams;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAdminConfig {

    @Value("${topics.kafka.users-in}")
    private String usersTopics;
    @Value("${topics.kafka.news-in}")
    private String newsTopics;
    @Value("${topics.kafka.auths-out}")
    private String authTopic;

    @Value("${topics.kafka.listcom-out}")
    private String listcomTopics;
    @Value("${topics.kafka.partitioncom-out}")
    private String partitioncomTopics;
    @Value("${topics.kafka.paymentcom-out}")
    private String paymentcomTopics;
    @Value("${topics.kafka.balancecom-out}")
    private String balancecomTopics;
    @Value("${topics.kafka.checkout-out}")
    private String checkoutTopics;
    @Value("${topics.kafka.usersHistories-out}")
    private String usersHistoriesTopics;

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(configs);
    }

//    @Bean
//    public NewTopic sendTopic() {
//        return TopicBuilder.name(pageviewsTopics)
//                .partitions(1)
//                .replicas(1)
//                .compact()
//                .build();
//    }
    @Bean
    public NewTopic countTopic() {
        return TopicBuilder.name(authTopic)
                .partitions(1)
                .replicas(1)
//                .compact()
                .build();
    }
    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name(newsTopics)
                .partitions(1)
                .replicas(1)
//                .compact()
                .build();
    }
    @Bean
    public NewTopic receiveTopic() {
        return TopicBuilder.name(usersTopics)
                .partitions(1)
                .replicas(1)
//                .compact()
//                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
                .build();
    }

//    @Bean
//    public NewTopic topic3() {
//        return TopicBuilder.name("thing3")
//                .assignReplicas(0, Arrays.asList(0, 1))
//                .assignReplicas(1, Arrays.asList(1, 2))
//                .assignReplicas(2, Arrays.asList(2, 0))
//                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
//                .build();
//    }
@Bean
public NewTopic listTopic() {
    return TopicBuilder.name(listcomTopics)
            .partitions(1)
            .replicas(1)
//                .compact()
            .build();
}
    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder.name(paymentcomTopics)
                .partitions(1)
                .replicas(1)
//                .compact()
                .build();
    }
    @Bean
    public NewTopic balanceTopic() {
        return TopicBuilder.name(balancecomTopics)
                .partitions(1)
                .replicas(1)
//                .compact()
                .build();
    }
    @Bean
    public NewTopic partitionTopic() {
        return TopicBuilder.name(partitioncomTopics)
                .partitions(1)
                .replicas(1)
//                .compact()
                .build();
    }
    @Bean
    public NewTopic historyTopic() {
        return TopicBuilder.name(usersHistoriesTopics)
                .partitions(1)
                .replicas(1)
//                .compact()
                .build();
    }
    @Bean
    public NewTopic checkoutTopic() {
        return TopicBuilder.name(checkoutTopics)
                .partitions(1)
                .replicas(1)
//                .compact()
                .build();
    }
}
