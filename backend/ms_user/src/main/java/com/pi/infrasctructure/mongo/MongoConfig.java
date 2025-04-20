package com.pi.infrasctructure.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}") String URI;
    @Value("${spring.data.mongodb.database}") String DB_NAME;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(URI);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), DB_NAME);
    }
}
