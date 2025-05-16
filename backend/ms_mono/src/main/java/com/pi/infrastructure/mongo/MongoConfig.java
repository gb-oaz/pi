package com.pi.infrastructure.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}") String URI;
    @Value("${spring.data.mongodb.database}") String DB_NAME;
    @Value("${spring.data.mongodb.collections.users}") String COLLECTION_USERS;
    @Value("${spring.data.mongodb.collections.quizes}") String COLLECTION_QUIZES;
    @Value("${spring.data.mongodb.collections.lives}") String COLLECTION_LIVES;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(URI);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, DB_NAME);
    }

    @Bean
    public ApplicationRunner initCollections(MongoTemplate mongoTemplate) {
        return args -> {
            if (!mongoTemplate.collectionExists(COLLECTION_USERS)) mongoTemplate.createCollection(COLLECTION_USERS);
            if (!mongoTemplate.collectionExists(COLLECTION_QUIZES)) mongoTemplate.createCollection(COLLECTION_QUIZES);
            if (!mongoTemplate.collectionExists(COLLECTION_LIVES)) mongoTemplate.createCollection(COLLECTION_LIVES);
        };
    }
}
