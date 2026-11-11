package dev.robertfuller.little_thing.repositories;

import dev.robertfuller.little_thing.models.RequestedRandomNumber;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RandomNumberRepository extends MongoRepository<RequestedRandomNumber, String> {
}
