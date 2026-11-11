# LittleThing

LittleThing is a deliberately small Spring Boot application built as a **playground for exploring Spring testing patterns**. The application itself is trivial (it hands out random numbers and records each request in MongoDB) so that the interesting part, *how each layer gets tested*, stays easy to see.

The goal is to show, side by side, how one feature can be tested at each level of the testing pyramid: plain unit tests, Mockito-driven service tests, Spring context tests, and full end-to-end tests against a real database running in a container.

## The application

A single endpoint returns a random integer in an inclusive range:

```
GET /v0/random/integer/in_range?min=1&max=10
```

```json
{
  "data": {
    "number": 7,
    "min": 1,
    "max": 10
  }
}
```

Every request is saved to MongoDB as a `RequestedRandomNumber` document, along with the time it was made.

### Request flow

```
RandomController  ──►  RandomNumberService  ──►  RandomNumberRepository  ──►  MongoDB
                            │        │
                         Random   InstantGiver
```

| Layer | Class | Role |
|---|---|---|
| Web | `controllers/RandomController` | Maps the HTTP request and wraps the result in `Response<T>` |
| Service | `services/RandomNumberService` | Generates the number, stamps the time, saves the record |
| Persistence | `repositories/RandomNumberRepository` | Spring Data `MongoRepository` |
| Model | `models/RequestedRandomNumber` | MongoDB document (`@Document`, indexed fields) |
| DTOs | `dtos/Response`, `dtos/RandomNumberResponse` | Immutable Lombok `@Value` / `@Jacksonized` response types |
| Config | `configuration/RandomerConfig`, `configuration/TimeConfiguration` | Provide `Random` and `InstantGiver` as beans |

### Designed for testability

The two sources of non-determinism, randomness and the clock, are **injected as Spring beans** rather than created inline:

- `java.util.Random` is provided by `RandomerConfig`.
- The current time comes from `TimeConfiguration.InstantGiver`.

Because of this, tests can swap either one for a mock and make assertions about exact values. Much of the project is about this one idea.

## Testing patterns demonstrated

| Pattern | Example | What it shows |
|---|---|---|
| **Plain unit tests** | `dtos/ResponseTests`, `dtos/RandomNumberResponseTests`, `models/RequestedRandomNumberTests`, `configuration/RandomerConfigTests` | Testing builders, factory methods and config classes with no Spring context at all |
| **Mockito unit tests** | `services/RandomNumberServiceTests` | `@ExtendWith(MockitoExtension.class)` with `@Mock` collaborators, constructor injection of the subject, stubbing with `when(...)`, and checking interactions with `verify(...)` |
| **Static mocking** | `LittleThingsUnitTests` | `mockStatic(SpringApplication.class)` to check that `main` delegates to `SpringApplication.run` without starting the app |
| **Context load test** | `LittleThingTests` | `@SpringBootTest` smoke test that the whole application context wires up |
| **Testcontainers + `@ServiceConnection`** | `configuration/MongTestContainersConfig` | A reusable `@TestConfiguration` that starts `mongo:8.0` in Docker and connects Spring Data to it automatically |
| **End-to-end HTTP tests** | `controllers/RandomControllerEndToEndTests` | `@SpringBootTest(webEnvironment = RANDOM_PORT)` with `TestRestTemplate`, `@MockitoBean` replacing the `Random` bean in the live context, checking the persisted document through `MongoTemplate`, AssertJ recursive comparison that ignores generated fields, and cleaning up in `@AfterEach` |
| **Controller slice tests** *(planned)* | `controllers/RandomControllerIntegrationTests` | Placeholder for web-layer tests (for example with `@WebMvcTest`/`MockMvc`) |

### Unit vs. end-to-end: the same behaviour at two levels

`RandomNumberServiceTests` and `RandomControllerEndToEndTests` both check that the service asks `Random` for `nextInt(min, max + 1)` and saves what it gets back:

- The **unit test** builds the service by hand from mocks. It runs in milliseconds and checks the exact object passed to `repository.save(...)`.
- The **end-to-end test** sends a real HTTP request through a running server, mocks only `Random` with `@MockitoBean`, and reads the result back from a real MongoDB instance.

Comparing the two shows what each kind of test gives you and what it costs.

## Tech stack

- Java 22
- Spring Boot 4.1 (Web MVC, Data MongoDB)
- Lombok
- JUnit 5, Mockito, AssertJ
- Testcontainers (MongoDB)
- Maven

## Getting started

### Prerequisites

- JDK 22+
- Maven
- Docker (needed for Testcontainers-backed tests)

### Running the tests

```bash
mvn test
```

Docker must be running. The Testcontainers-backed tests (`LittleThingTests`, `RandomControllerEndToEndTests`) start a throwaway MongoDB container on their own, so no local database is needed. The surefire plugin is set up to load Mockito as a Java agent, which keeps inline/static mocking working on recent JDKs.

### Running the application

The app reads its MongoDB connection string from the `DATA_URI` environment variable:

```bash
DATA_URI=mongodb://localhost:27017/little_thing mvn spring-boot:run
```

Then:

```bash
curl "http://localhost:8080/v0/random/integer/in_range?min=1&max=10"
```

## Project layout

```
src/
├── main/java/dev/robertfuller/little_thing/
│   ├── LittleThing.java              # Spring Boot entry point
│   ├── configuration/                # Random and clock beans
│   ├── controllers/                  # REST endpoint
│   ├── dtos/                         # Response wrappers
│   ├── models/                       # MongoDB documents
│   ├── repositories/                 # Spring Data repositories
│   └── services/                     # Business logic
└── test/java/dev/robertfuller/little_thing/
    ├── configuration/                # Testcontainers config + config tests
    ├── controllers/                  # End-to-end / web-layer tests
    ├── dtos/                         # DTO unit tests
    ├── models/                       # Model unit tests
    └── services/                     # Mockito service tests
```

The test tree mirrors the main source tree, so each class's tests are easy to find.
