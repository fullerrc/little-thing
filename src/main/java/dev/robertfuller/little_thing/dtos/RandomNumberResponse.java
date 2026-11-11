package dev.robertfuller.little_thing.dtos;

import dev.robertfuller.little_thing.models.RequestedRandomNumber;

@lombok.Builder
@lombok.extern.jackson.Jacksonized
@lombok.Value
public class RandomNumberResponse {
    int number;
    int min;
    int max;

    public static RandomNumberResponse of(RequestedRandomNumber requestedRandomNumber) {
        return RandomNumberResponse.builder()
                .min(requestedRandomNumber.getMin())
                .max(requestedRandomNumber.getMax())
                .number(requestedRandomNumber.getNumber())
                .build();
    }
}
