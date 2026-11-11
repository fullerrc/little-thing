package dev.robertfuller.little_thing;

import dev.robertfuller.little_thing.configuration.MongTestContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(MongTestContainersConfig.class)
public class LittleThingTests {
    @Test
    void contextLoads() {}
}
