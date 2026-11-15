# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Stack

Spring Boot 4.1.2 (Spring MVC via `spring-boot-starter-webmvc`), Java 27, Lombok, Maven (wrapper pins Maven 3.10.0). No database or persistence layer yet.

## Commands

Use the Maven wrapper (`./mvnw` in Git Bash, `.\mvnw.cmd` in PowerShell):

- Build: `./mvnw clean package`
- Run the app (port 8080 by default): `./mvnw spring-boot:run`
- All tests: `./mvnw test`
- Single test class: `./mvnw test -Dtest=EcomApplicationTests`
- Single test method: `./mvnw test -Dtest=EcomApplicationTests#contextLoads`

There is no linter or formatter configured.

## Architecture

Everything currently lives flat in the `com.app.ecom` package; there are no separate controller/service/repository layers yet.

- `EcomApplication` — Spring Boot entry point.
- `User` — Lombok `@Data` POJO (`id`, `firstName`, `lastName`).
- `UserController` — `@RestController` exposing `GET /api/users` and `POST /api/users`. Users are held in an in-memory `ArrayList` field on the controller (singleton bean), so data is lost on restart and the list is not thread-safe. `POST` returns the full list rather than the created user.

## Documentation

All code documentation goes in `docs/` as Markdown files (e.g. `docs/<layer>.md`). Check there for coding standards before changing a layer.

## Build notes

- Lombok is wired as an explicit annotation processor in `maven-compiler-plugin` for both `compile` and `testCompile` executions. Keep that config if adding other annotation processors (e.g. MapStruct) — add them to the same `annotationProcessorPaths`.
- The POM contains intentionally empty `<licenses>`, `<developers>`, `<scm>` elements to block inheritance from the Spring Boot parent (see `HELP.md`); don't remove them.
