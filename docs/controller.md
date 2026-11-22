# Controller Layer

Controllers are the HTTP entry point. They map URLs to methods, turn JSON into Java objects and back, call the service layer, and choose the HTTP status code. They contain no business logic.

## Conventions

**Status: current**

- Annotate the class with `@RestController` and set a base path with `@RequestMapping("/api/<resource>")`. Use plural resource names (`/api/users`).
- Use `@GetMapping`, `@PostMapping`, `@PutMapping` and `@DeleteMapping` instead of `@RequestMapping(method = ...)`.
- Inject dependencies through the constructor: declare `private final` fields and add Lombok's `@RequiredArgsConstructor`.
- Return `ResponseEntity<T>` so the status code is explicit.
- Turn an `Optional` from the service into `200` or `404` like this:
  ```java
  return userService.fetchUser(id)
          .map(ResponseEntity::ok)
          .orElseGet(() -> ResponseEntity.notFound().build());
  ```
- Don't keep state in the controller. Controllers are singletons, so any field is shared by every request.
- Call services only, never repositories (see [architecture.md](architecture.md)).

## Current controllers

### `UserController` — `src/main/java/com/app/ecom/UserController.java`

**Status: current**

Base path: `/api/users`. Dependency: `UserService` (constructor-injected).

| Method | Mapping | Calls | Returns |
|---|---|---|---|
| `getAllUsers()` | `GET /api/users` | `userService.fetchallUsers()` | `200` + `List<User>` |
| `getAllUsers(long id)` | `GET /api/users/{id}` | `userService.fetchUser(id)` | `200` + `User`, or `404` with an empty body |
| `createUsers(User user)` | `POST /api/users` | `userService.createUser(user)` | `200` + `"User added successfully"` |
| `updateUsers(long id, User updatedUser)` | `PUT /api/users/{id}` | `userService.updateUser(id, updatedUser)` | `200` + `"User updated successfully"`, or `404` |

Request and response examples are in [api.md](api.md).

#### Known issues

- An unused `private List<User> userList` field is left over from before the service layer existed. Storage now lives in `UserService`.
- Unused imports: `Autowired`, `WebMvcProperties`.
- The GET-by-id method is named `getAllUsers` (an overload). `getUserById` would describe it better.
- `POST` returns `200` with a text message. REST convention is `201 Created` with the created user (or a `Location` header).
- `PUT` and `GET /{id}` return an empty `404` body with no error message. A `@RestControllerAdvice` could standardise error responses.
- The request bodies have no validation (`@Valid` and Jakarta Bean Validation aren't set up).

## Adding a new controller

1. Create `<Resource>Controller` with `@RestController` and `@RequestMapping("/api/<resources>")`.
2. Inject the matching service through the constructor.
3. Add a section for it here, add its endpoints to [api.md](api.md) and to the endpoint table in [../README.md](../README.md).
