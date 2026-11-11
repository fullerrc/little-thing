package dev.robertfuller.little_thing.services;

import dev.robertfuller.little_thing.configuration.TimeConfiguration;
import dev.robertfuller.little_thing.dtos.RandomNumberResponse;
import dev.robertfuller.little_thing.models.RequestedRandomNumber;
import dev.robertfuller.little_thing.repositories.RandomNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomNumberService {
    private final Random random;
    private final RandomNumberRepository randomNumberRepository;
    private final TimeConfiguration.InstantGiver instantGiver;
    public RandomNumberResponse randomNumber(int min, int max) throws IllegalArgumentException {
        RequestedRandomNumber requestedRandomNumber = RequestedRandomNumber.builder()
                .number(random.nextInt(min, max + 1))
                .min(min)
                .max(max)
                .time(instantGiver.getInstantMillis())
                .build();
        RandomNumberResponse response = RandomNumberResponse.of(requestedRandomNumber);

        randomNumberRepository.save(requestedRandomNumber);

        return response;
    }
}
