package dev.robertfuller.little_thing.models;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestedRandomNumberTests {
    @Test
    public void testInstantiation() {
        Instant time = Instant.now();
        RequestedRandomNumber subject = RequestedRandomNumber.builder()
                .min(1)
                .max(2)
                .number(43)
                .time(time.toEpochMilli())
                .build();
        assertEquals(1, subject.getMin());
        assertEquals(2, subject.getMax());
        assertEquals(43, subject.getNumber());
        assertEquals(time.toEpochMilli(), subject.getTime());
    }
}
