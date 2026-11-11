package dev.robertfuller.little_thing.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mongodb.MongoDBContainer;

@TestConfiguration(proxyBeanMethods = false)
public class MongTestContainersConfig {
    @Bean
    @ServiceConnection
    MongoDBContainer mongoDBContainer() { return new MongoDBContainer("mongo:8.0");}
}
