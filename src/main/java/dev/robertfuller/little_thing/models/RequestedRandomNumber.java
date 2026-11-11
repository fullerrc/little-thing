package dev.robertfuller.little_thing.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@lombok.Builder
@lombok.Value
@Document
public class RequestedRandomNumber {
    @Id
    String id;
    @Indexed
    long time;
    @Indexed
    int min;
    @Indexed
    int max;
    @Indexed
    int number;
}
