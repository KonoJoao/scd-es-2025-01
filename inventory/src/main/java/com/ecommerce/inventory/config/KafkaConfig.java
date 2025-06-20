package com.ecommerce.inventory.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Autowired
    private Environment env;

    @Value("${kafka.topic}")
    private String topic;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configs = new HashMap<>();

        configs.put("bootstrap.servers", env.getProperty("bootstrap.servers"));
        configs.put("key.serializer", env.getProperty("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"));
        configs.put("value.serializer", env.getProperty("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"));

        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic inventoryTopicBuilder() {
        return TopicBuilder
                .name(topic)
                .partitions(env.getProperty("spring.kafka.topic.partitions", Integer.class, 1))
                .replicas(env.getProperty("spring.kafka.topic.replicas", Integer.class, 1))
                .build();
    }
}