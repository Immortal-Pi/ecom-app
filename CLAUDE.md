# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Stack

Spring Boot 4.1.2 (Spring MVC via `spring-boot-starter-webmvc`), Java 27, Lombok, Maven (wrapper pins Maven 3.10.0). No database or persistence layer yet. Spring Data JPA is planned (see [docs/configuration.md](docs/configuration.md#database--jpa)).

## Commands

Use the Maven wrapper (`./mvnw` in Git Bash, `.\mvnw.cmd` in PowerShell):

- Build: `./mvnw clean package`
- Run the app (port 8080 by default): `./mvnw spring-boot:run`
- All tests: `./mvnw test`
- Single test class: `./mvnw test -Dtest=EcomApplicationTests`
- Single test method: `./mvnw test -Dtest=EcomApplicationTests#contextLoads`

There is no linter or formatter configured.

## Architecture

Request flow: `Client → Controller → Service → (Repository → Database, planned)`. All classes currently sit in the single package `com.app.ecom`, with `EcomApplication` as the entry point. Details: [docs/architecture.md](docs/architecture.md).

- **Controllers** (`@RestController`, e.g. `UserController` on `/api/users`): handle HTTP only (routing, request/response JSON, status codes) and delegate to services. → [docs/controller.md](docs/controller.md)
- **Services** (`@Service`, e.g. `UserService`): hold business logic. `UserService` also stores users in an in-memory `ArrayList` with an id counter, so data is lost on restart and storage isn't thread-safe. → [docs/service.md](docs/service.md)
- **Model** (`User`, a Lombok `@Data` POJO with `id`, `firstName`, `lastName`): will become a JPA `@Entity`. → [docs/model.md](docs/model.md)
- **Repositories** (planned): Spring Data JPA interfaces (`JpaRepository`) handling all database operations through entities instead of SQL. Only services call them. → [docs/repository.md](docs/repository.md)

Endpoint reference: [docs/api.md](docs/api.md). Config and the planned DB setup: [docs/configuration.md](docs/configuration.md). Tests: [docs/testing.md](docs/testing.md).

## Documentation maintenance (required)

Detailed docs live in `docs/` (index and code→doc mapping: [docs/README.md](docs/README.md)). This file and `README.md` hold only the high-level overview. **Every code change must update the matching docs in the same change**:

- Read the relevant `docs/<layer>.md` for conventions before changing a layer.
- Controller or endpoint change → `docs/controller.md`, `docs/api.md`, and the API table in `README.md`.
- Service change → `docs/service.md`. Model or entity change → `docs/model.md`. Repository or query change → `docs/repository.md`.
- New dependency, property or plugin → `docs/configuration.md` (and the Stack line above if it's significant).
- New layer or area (DTOs, exception handling, security, validation, ...) → create `docs/<area>.md` and link it from `docs/README.md`, `README.md` and the Architecture section above.
- When something marked **Status: planned** is built (e.g. JPA/repositories), rewrite that section as **current**. Remove fixed items from **Known issues** lists, and add new ones you notice.
- Keep this Architecture section and `README.md` in sync with the actual layers.

## Build notes

- Lombok is wired as an explicit annotation processor in `maven-compiler-plugin` for both `compile` and `testCompile` executions. Keep that config if adding other annotation processors (e.g. MapStruct) — add them to the same `annotationProcessorPaths`.
- The POM contains intentionally empty `<licenses>`, `<developers>`, `<scm>` elements to block inheritance from the Spring Boot parent (see `HELP.md`); don't remove them.
