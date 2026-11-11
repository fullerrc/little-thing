package dev.robertfuller.little_thing;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

public class LittleThingsUnitTests {
    @Test
    public void testMainDelegatesToSpringApplication() {
        String[] args = {"--foo=bar"};
        try (MockedStatic<SpringApplication> spring = mockStatic(SpringApplication.class)) {
            LittleThing.main(args);
            spring.verify(() -> SpringApplication.run(LittleThing.class, args));
        }
    }
}
