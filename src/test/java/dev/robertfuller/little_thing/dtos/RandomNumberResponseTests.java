package dev.robertfuller.little_thing.dtos;

import dev.robertfuller.little_thing.models.RequestedRandomNumber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomNumberResponseTests {
    @Test
    public void testInstantiation() {
        int number = 123;
        int min = 1;
        int max = 1234;
        RandomNumberResponse actual = RandomNumberResponse.builder()
                .number(number)
                .min(min)
                .max(max)
                .build();
        assertEquals(number, actual.getNumber());
        assertEquals(min, actual.getMin());
        assertEquals(max, actual.getMax());

    }

    @Test
    public void testOf() {
        int number = 123;
        int min = 1;
        int max = 1234;

        RequestedRandomNumber requestedRandomNumber = RequestedRandomNumber.builder()
                .min(min)
                .max(max)
                .number(number)
                .time(0L)
                .build();

        RandomNumberResponse expected = RandomNumberResponse.builder()
                .number(number)
                .min(min)
                .max(max)
                .build();
        RandomNumberResponse actual = RandomNumberResponse.of(requestedRandomNumber);

        assertEquals(expected, actual);


    }
}
