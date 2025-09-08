package com.ecommerce.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {
    @Bean
    public NewTopic cartItemAddedTopic(@Value("${topics.cartItemAdded}") String name) {
        return TopicBuilder.name(name)
                .partitions(1)
                .replicas(1)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "1")
                .build();
    }
}
