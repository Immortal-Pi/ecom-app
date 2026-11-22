# Documentation Index

Detailed documentation for the ecom-application. The high-level overview lives in the root [README.md](../README.md) and in [CLAUDE.md](../CLAUDE.md); this folder holds the per-layer details.

## Documents

| Doc | What it covers |
|---|---|
| [architecture.md](architecture.md) | Layers, request flow, package layout (current and target), rules between layers |
| [controller.md](controller.md) | REST controller conventions and the current `UserController` |
| [service.md](service.md) | Service-layer conventions and the current `UserService` |
| [model.md](model.md) | Domain model (`User`) and JPA entity conventions |
| [repository.md](repository.md) | Data-access layer with Spring Data JPA (**planned**) |
| [api.md](api.md) | HTTP endpoint reference with request/response examples |
| [configuration.md](configuration.md) | Stack, build, `application.properties`, database setup (**planned**) |
| [testing.md](testing.md) | Current tests and the testing approach per layer |

## Which doc to update when code changes

| If you change... | Update |
|---|---|
| A `@RestController` class, or add/remove/rename an endpoint | [controller.md](controller.md), [api.md](api.md), endpoint table in [../README.md](../README.md) |
| A `@Service` class | [service.md](service.md) |
| A domain class / `@Entity`, its fields or relationships | [model.md](model.md) |
| A Spring Data repository interface or custom query | [repository.md](repository.md) |
| Packages, a new layer, or how layers call each other | [architecture.md](architecture.md), plus the overview in [../CLAUDE.md](../CLAUDE.md) and [../README.md](../README.md) |
| `pom.xml` dependencies or plugins, `application.properties`, profiles | [configuration.md](configuration.md), plus the Stack line in [../CLAUDE.md](../CLAUDE.md) |
| Tests or test tooling | [testing.md](testing.md) |
| DTOs, exception handlers, security, validation (new areas) | Create a new `docs/<area>.md`, then link it here, in [../README.md](../README.md) and in [../CLAUDE.md](../CLAUDE.md) |

## How to update the docs

1. Change the code.
2. Find the affected docs in the table above and update them in the same change (same commit or PR).
3. If you built something marked **Status: planned**, rewrite that section as **Status: current** and describe what exists now.
4. If you fixed something listed under **Known issues**, remove it from the list. If you noticed a new one, add it.
5. Base code snippets on the real source. Label any snippet that isn't in the code yet as an example.
6. Use relative links so they work on GitHub and in the IDE.
