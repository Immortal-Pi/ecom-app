# Service Layer

Services hold the business logic. Controllers call them, and once persistence is added they will call repositories. They don't know about HTTP.

## Conventions

**Status: current**

- Annotate with `@Service`. Name the class `<Resource>Service`.
- Return domain types, `Optional<T>` for lookups that may find nothing, and `boolean` (or throw a custom exception) for operations that can fail on a missing record. Don't return `ResponseEntity` or `HttpStatus`.
- Don't use web annotations (`@RequestBody`, `@PathVariable`) on service methods. They only mean something in controllers.
- Keep storage private. Other classes go through service methods, not fields.
- Once JPA is in: inject repositories through the constructor (`private final` + `@RequiredArgsConstructor`), and put `@Transactional` on methods that write.

## Current services

### `UserService` — `src/main/java/com/app/ecom/UserService.java`

**Status: current (in-memory storage)**

State:
- `public List<User> userList = new ArrayList<>()`: the user "store", held only in memory.
- `private Long nextId = 1L`: a counter used to assign ids.

| Method | Behaviour |
|---|---|
| `List<User> fetchallUsers()` | Returns the internal list itself, not a copy. |
| `void createUser(User user)` | Sets `user.id = nextId++` (ignoring any id the client sent) and appends the user to the list. |
| `Optional<User> fetchUser(Long id)` | Searches the list with a stream for a matching id. |
| `boolean updateUser(Long id, User updatedUser)` | Finds the user by id and copies `firstName` and `lastName` from `updatedUser`. Returns `false` if there's no user with that id. |

#### Known issues

- `userList` is `public` and mutable, and `fetchallUsers()` returns the live list, so callers can change the stored data.
- Not thread-safe. `ArrayList` and `nextId++` are shared by every request in a singleton bean, so concurrent `POST`s can collide.
- `createUser` has a stray `@RequestBody` annotation on its parameter (and imports `PathVariable`/`RequestBody`).
- The find-by-id stream is repeated in `fetchUser` and `updateUser`. `updateUser` could call `fetchUser`.
- Data is lost on restart. This goes away with the JPA migration below.

## After JPA

**Status: planned**

When [repository.md](repository.md) is built, `UserService` will delegate storage to `UserRepository`, and `userList`/`nextId` will be removed because the database generates the ids. Example of the target shape:

```java
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> fetchAllUsers()          { return userRepository.findAll(); }
    public Optional<User> fetchUser(Long id)   { return userRepository.findById(id); }

    @Transactional
    public User createUser(User user)          { return userRepository.save(user); }

    @Transactional
    public boolean updateUser(Long id, User updated) {
        return userRepository.findById(id).map(existing -> {
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            return true;   // dirty checking flushes the change at commit
        }).orElse(false);
    }
}
```

When this happens, rewrite the "Current services" section above and change this section to "current".
