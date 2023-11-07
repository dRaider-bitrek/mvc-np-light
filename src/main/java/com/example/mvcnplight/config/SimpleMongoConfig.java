package com.example.mvcnplight.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class SimpleMongoConfig {
    private final String LOCAL = "mongodb://root:example@localhost:27017";
//    private final String DOCKER = "mongodb://root:example@mongo:27017";
//    private final String DOCKER = "mongodb://bitrek:examplebitrek@mongo:27017";
//    private final String DOCKER_BS = "mongodb://navitrek:Fjo4ojGpgpi@mongo:27017";
    private final String DOCKER_NP = "mongodb://np_test:nb_bitrek@mongo:27017";

    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(DOCKER_NP);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), "light");
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
