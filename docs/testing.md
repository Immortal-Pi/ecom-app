# Testing

The tests that exist today and how each layer should be tested.

## Current tests

**Status: current**

| Test | What it checks |
|---|---|
| `src/test/java/com/app/ecom/EcomApplicationTests.java` → `contextLoads` | `@SpringBootTest`: the whole Spring context starts |

Commands are in [configuration.md](configuration.md#commands) (`./mvnw test`, `-Dtest=Class#method`).

## Approach per layer

**Status: planned. Add a row to the table above for each test class you write.**

| Layer | Test style | Notes |
|---|---|---|
| Controller | `@WebMvcTest(UserController.class)` with `MockMvc`, service mocked through `@MockitoBean` | Checks routes, status codes and JSON without starting the full app. In Spring Boot 4 the test-slice annotations live in per-module packages (for example `org.springframework.boot.webmvc.test.autoconfigure`), so let the IDE resolve imports. |
| Service | Plain JUnit 5 + Mockito (`@ExtendWith(MockitoExtension.class)`), repository mocked | Fast. No Spring context. |
| Repository | `@DataJpaTest` (needs the JPA test starter once JPA is added) | Runs against an embedded database. Test only custom or derived queries, not the built-in CRUD methods. |
| End-to-end | `@SpringBootTest(webEnvironment = RANDOM_PORT)` with a REST client | Use sparingly. |

## Conventions

- Mirror the main package: tests for `com.app.ecom.service.UserService` go in `src/test/java/com/app/ecom/UserServiceTest.java`.
- Name test methods after the behaviour: `fetchUser_returnsEmpty_whenIdUnknown`.
- Any new endpoint should come with at least one controller test for the success path and one for the not-found or invalid path.
