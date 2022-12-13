package com.example.UnitTestKafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.replication.factor}")
    private int replicat;
    @Value("${spring.kafka.partition.number}")
    private int partitions;

    @Value("${spring.kafka.topic.name}")
    private String topicJsonName;

    @Bean
    public NewTopic javaguidesJsonTopic() {
        return TopicBuilder.name(topicJsonName).partitions(partitions).replicas(replicat)
                .build();
    }
}