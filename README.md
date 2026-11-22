# ecom-application

An e-commerce backend built with Spring Boot. Right now it exposes a REST API for managing users, stored in memory. The next step is persistence with Spring Data JPA.

## Tech stack

Java 27 · Spring Boot 4.1.2 (Spring MVC) · Lombok · Maven (wrapper). Details: [docs/configuration.md](docs/configuration.md).

## Quick start

```bash
./mvnw spring-boot:run      # starts on http://localhost:8080
./mvnw test                 # runs tests
./mvnw clean package        # builds the jar
```
(On PowerShell, use `.\mvnw.cmd`.)

## Layers at a glance

| Layer | Handles | Status | Details |
|---|---|---|---|
| **Controller** | HTTP: URL mapping, request/response JSON, status codes. Delegates to services. | current | [docs/controller.md](docs/controller.md) |
| **Service** | Business logic: id assignment, lookups, updates. Currently also stores data in memory. | current | [docs/service.md](docs/service.md) |
| **Model** | Data classes (`User`). Will become JPA `@Entity` classes. | current | [docs/model.md](docs/model.md) |
| **Repository** | Database access through Spring Data JPA entities, without hand-written SQL. | planned | [docs/repository.md](docs/repository.md) |

Request flow: `Client → Controller → Service → (Repository → Database)`. See [docs/architecture.md](docs/architecture.md).

## API

| Method | Path | Description |
|---|---|---|
| GET | `/api/users` | List all users |
| GET | `/api/users/{id}` | Get one user (404 if missing) |
| POST | `/api/users` | Create a user |
| PUT | `/api/users/{id}` | Update a user's names (404 if missing) |

Examples: [docs/api.md](docs/api.md).

## Documentation

All detailed docs are in [`docs/`](docs/README.md):
[architecture](docs/architecture.md) ·
[controller](docs/controller.md) ·
[service](docs/service.md) ·
[model](docs/model.md) ·
[repository](docs/repository.md) ·
[api](docs/api.md) ·
[configuration](docs/configuration.md) ·
[testing](docs/testing.md)

When you change code, update the matching doc in the same change. The mapping is in [docs/README.md](docs/README.md#which-doc-to-update-when-code-changes).
