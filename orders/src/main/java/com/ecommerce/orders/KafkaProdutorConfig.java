package com.ecommerce.orders;

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
public class KafkaProdutorConfig {

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

        System.out.println(configs);

        // Configurações adicionais (opcionais)
//        if (env.containsProperty("spring.kafka.producer.acks")) {
//            configs.put("acks", env.getProperty("spring.kafka.producer.acks"));
//        }
//        if (env.containsProperty("spring.kafka.producer.retries")) {
//            configs.put("retries", env.getProperty("spring.kafka.producer.retries"));
//        }

        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic pagamentoRequestTopicBuilder() {
        return TopicBuilder
                .name(topic)
                .partitions(env.getProperty("spring.kafka.topic.partitions", Integer.class, 1))
                .replicas(env.getProperty("spring.kafka.topic.replicas", Integer.class, 1))
                .build();
    }
}