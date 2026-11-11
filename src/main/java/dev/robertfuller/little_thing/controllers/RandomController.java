package dev.robertfuller.little_thing.controllers;

import dev.robertfuller.little_thing.dtos.RandomNumberResponse;
import dev.robertfuller.little_thing.dtos.Response;
import dev.robertfuller.little_thing.services.RandomNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v0/random")
@RequiredArgsConstructor
public class RandomController {
    private final RandomNumberService randomNumberService;

    @GetMapping(value = "/integer/in_range")
    public Response<RandomNumberResponse> getInInRange(@RequestParam("min") int min, @RequestParam("max") int max) {
        return Response.of(randomNumberService.randomNumber(min, max));
    }
}
