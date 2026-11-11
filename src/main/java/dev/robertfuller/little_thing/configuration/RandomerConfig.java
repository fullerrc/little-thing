package dev.robertfuller.little_thing.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class RandomerConfig {
    @Bean
    Random getRandom() { return new Random(); }
}
