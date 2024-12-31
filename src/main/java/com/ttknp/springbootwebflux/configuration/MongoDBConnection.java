package com.ttknp.springbootwebflux.configuration;

// *********

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ttknp.springbootwebflux.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import static java.util.Collections.singletonList;

@Configuration
@EnableMongoRepositories(basePackageClasses = EmployeeRepo.class)
@EnableConfigurationProperties
public class MongoDBConnection {

    // ** MongoProperties class is working for config mongodb you can use application.property instead this. (optional)
    @Primary
    @Bean(name = "mongodbTTKNPProperties")
    @ConfigurationProperties(prefix = "mongodb.ttknp") // specify prefix for mapping to property file
    public MongoProperties mongodbTTKNPProperties() {
        return new MongoProperties();
    }

    @Bean(name = "mongodbLocalProperties")
    @ConfigurationProperties(prefix = "mongodb.local")
    public MongoProperties mongodbLocalProperties() {
        return new MongoProperties();
    }

    @Bean
    public MongoClient mongoClientConfig(@Qualifier("mongodbLocalProperties") MongoProperties mongoProperties) {
        MongoCredential credential = MongoCredential
                .createCredential(mongoProperties.getUsername(),
                        mongoProperties.getAuthenticationDatabase(), // ** importance
                        mongoProperties.getPassword());
        return MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder
                        .hosts(singletonList(new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort()))))
                .credential(credential)
                .build());
    }

}