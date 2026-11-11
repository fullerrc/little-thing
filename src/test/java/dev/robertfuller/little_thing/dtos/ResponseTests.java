package dev.robertfuller.little_thing.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTests {
    @Test
    public void testOf() {
        Response<Integer> actual = Response.of(3);

        assertEquals(3, actual.getData());
    }
}
