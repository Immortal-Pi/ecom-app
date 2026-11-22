# Architecture

How the application is put together: its layers, what each one does, and how a request moves through them.

## Layers

**Status: current (Controller → Service → in-memory list). Repository and database: planned.**

```
HTTP client
   │  JSON over HTTP
   ▼
Controller   (@RestController)   HTTP handling: routes, request/response, status codes
   │  Java method calls
   ▼
Service      (@Service)          business logic: ids, lookups, update rules
   │
   ▼
Repository   (JpaRepository)     database access through entities          [planned]
   │  Hibernate / JPA
   ▼
Database                                                                   [planned]
```

At the moment the service layer stores data itself in an in-memory `ArrayList`, so the data is lost on every restart. When Spring Data JPA is added, the service will call a repository instead, and the list will go away.

| Layer | Handles | Must not |
|---|---|---|
| Controller ([controller.md](controller.md)) | URL mapping, reading path variables and request bodies, choosing HTTP status codes, building `ResponseEntity` | Contain business logic, or call repositories directly |
| Service ([service.md](service.md)) | Business rules, id assignment, lookups, updates, transactions (once JPA is added) | Use HTTP types (`ResponseEntity`, `HttpStatus`, `@RequestBody`) |
| Repository ([repository.md](repository.md), planned) | Reading and writing entities with Spring Data JPA (`save`, `findById`, derived queries) | Contain business logic, or use hand-written SQL when a derived query or JPQL works |
| Model ([model.md](model.md)) | The shape of the data (`User`), and later JPA mapping annotations | Depend on the controller or service |

## Request flow (current)

| Request | Flow |
|---|---|
| `GET /api/users` | `UserController.getAllUsers()` → `UserService.fetchallUsers()` → returns the list → `200 OK` |
| `GET /api/users/{id}` | `UserController.getAllUsers(long id)` → `UserService.fetchUser(id)` returns an `Optional<User>` → `200 OK` with the user, or `404` |
| `POST /api/users` | `UserController.createUsers(user)` → `UserService.createUser(user)` assigns `nextId++` and adds the user → `200 OK` `"User added successfully"` |
| `PUT /api/users/{id}` | `UserController.updateUsers(id, user)` → `UserService.updateUser(id, user)` returns `true`/`false` → `200 OK` `"User updated successfully"`, or `404` |

Full endpoint details: [api.md](api.md).

## Package layout

**Current:** everything is in a single package, `com.app.ecom`:

```
src/main/java/com/app/ecom/
├── EcomApplication.java   Spring Boot entry point (@SpringBootApplication)
├── User.java              domain model (Lombok @Data)
├── UserController.java    REST controller for /api/users
└── UserService.java       business logic and in-memory storage
```

**Target (suggested once JPA lands):** split by layer. Sub-packages of `com.app.ecom` are still picked up by component scanning, because `EcomApplication` sits in the root package.

```
com.app.ecom
├── EcomApplication.java
├── controller/   UserController
├── service/      UserService
├── repository/   UserRepository  (extends JpaRepository)
├── model/        User            (@Entity)
├── dto/          request/response objects, if entities stop being exposed directly
└── exception/    @RestControllerAdvice and custom exceptions
```

If you move packages, update this section and the overview in [../CLAUDE.md](../CLAUDE.md).

## Dependency injection

Beans are wired through their constructors. `UserController` declares `private final UserService userService` and uses Lombok's `@RequiredArgsConstructor` to generate the constructor. Use the same pattern for new beans (services take repositories the same way) and avoid `@Autowired` on fields.
