package dev.robertfuller.little_thing.configuration;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomerConfigTests {
    @Test
    public void testGetRandom() {
        RandomerConfig subject = new RandomerConfig();

        Random actual = subject.getRandom();

        assertEquals(Random.class, actual.getClass());
    }

    @Test
    public void testInstantGiver() {
        TimeConfiguration.InstantGiver subject = new TimeConfiguration.InstantGiver();

        long actual = subject.getInstantMillis();

        assertTrue(actual > Instant.EPOCH.toEpochMilli());
    }
}
