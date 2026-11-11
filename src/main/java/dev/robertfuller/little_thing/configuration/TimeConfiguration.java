package dev.robertfuller.little_thing.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class TimeConfiguration {
    public static class InstantGiver {
        public long getInstantMillis() {
            return Instant.now().toEpochMilli();
        }
    }
    @Bean
    public InstantGiver instantGiver() {
        return new InstantGiver();
    }
}
