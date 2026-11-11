package dev.robertfuller.little_thing.services;

import dev.robertfuller.little_thing.configuration.TimeConfiguration;
import dev.robertfuller.little_thing.dtos.RandomNumberResponse;
import dev.robertfuller.little_thing.models.RequestedRandomNumber;
import dev.robertfuller.little_thing.repositories.RandomNumberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RandomNumberServiceTests {
    @Mock
    Random mockRandom;
    @Mock
    RandomNumberRepository mockRandomNumberRepository;
    @Mock
    TimeConfiguration.InstantGiver mockInstantGiver;

    RandomNumberService subject;

    @BeforeEach
    public void setUp() {
        subject = new RandomNumberService(mockRandom, mockRandomNumberRepository, mockInstantGiver);
    }

    @Test
    public void testRandomNumber() {
        long nowMillis = Instant.now().toEpochMilli();
        when(mockRandom.nextInt(anyInt(), anyInt())).thenReturn(42);
        when(mockInstantGiver.getInstantMillis()).thenReturn(nowMillis);

        RequestedRandomNumber expectedToSave = RequestedRandomNumber.builder()
                .min(0)
                .max(1)
                .number(42)
                .time(nowMillis)
                .build();
        RandomNumberResponse expected = RandomNumberResponse.of(expectedToSave);

        RandomNumberResponse actual = subject.randomNumber(0, 1);

        assertEquals(expected, actual);

        verify(mockRandom, times(1)).nextInt(0, 2);
        verify(mockRandomNumberRepository, times(1)).save(expectedToSave);


    }


}
