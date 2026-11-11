package dev.robertfuller.little_thing.controllers;

import dev.robertfuller.little_thing.configuration.MongTestContainersConfig;
import dev.robertfuller.little_thing.dtos.RandomNumberResponse;
import dev.robertfuller.little_thing.dtos.Response;
import dev.robertfuller.little_thing.models.RequestedRandomNumber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@Import(MongTestContainersConfig.class)
public class RandomControllerEndToEndTests {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @MockitoBean
    Random mockRandom;

    @AfterEach
    public void tearDown() {
        mongoTemplate.remove(new Query(), RequestedRandomNumber.class);
    }

    @Test
    public void testGetInRange() {
        when(mockRandom.nextInt(1, 11)).thenReturn(42);
        ResponseEntity<Response<RandomNumberResponse>> response = restTemplate.exchange(
                "http://localhost:" + port + "/v0/random/integer/in_range?min=1&max=10",
                HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(Response.of(RandomNumberResponse.builder().build()).getClass(), response.getBody().getClass());
        assertEquals(1, response.getBody().getData().getMin());
        assertEquals(10, response.getBody().getData().getMax());
        assertEquals(42, response.getBody().getData().getNumber());
        verify(mockRandom, times(1)).nextInt(1, 11);

        List<RequestedRandomNumber> inserted = mongoTemplate.findAll(RequestedRandomNumber.class);
        RequestedRandomNumber expected = RequestedRandomNumber.builder()
                .min(1)
                .max(10)
                .number(42)
                .build();

        assertEquals(1, inserted.size());
        assertThat(expected)
                .usingRecursiveComparison()
                .ignoringFields("id", "time")
                .isEqualTo(inserted.getFirst());
    }
}
