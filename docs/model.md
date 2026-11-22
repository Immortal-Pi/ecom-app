# Model Layer

The domain classes that describe the application's data. Once Spring Data JPA is added, these become JPA entities mapped to database tables.

## Current model

### `User` — `src/main/java/com/app/ecom/User.java`

**Status: current (plain Java object, not yet an entity)**

```java
@Data
public class User {
    private Long id;
    private String firstName;
    private String lastName;
}
```

| Field | Type | Notes |
|---|---|---|
| `id` | `Long` | Set by `UserService.createUser` from an in-memory counter. Any value the client sends is overwritten. |
| `firstName` | `String` | No validation. |
| `lastName` | `String` | No validation. |

Lombok's `@Data` generates the getters, setters, `equals`/`hashCode`, `toString` and a required-args constructor. The same class is used as the JSON request body, the JSON response and the stored object.

## JPA entity conventions

**Status: planned. Follow these when turning `User` (or any new class) into an entity.**

Example of the target `User` entity:

```java
@Entity
@Table(name = "users")            // "user" is a reserved word in several databases
@Getter
@Setter
@NoArgsConstructor                // JPA needs a no-args constructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;
}
```

Guidelines:
- **Avoid `@Data` on entities.** Its generated `equals`/`hashCode`/`toString` use every field, which breaks with lazy-loaded relationships (extra queries, `LazyInitializationException`, infinite recursion on two-way links) and with ids that change after `save`. Use `@Getter`/`@Setter`/`@NoArgsConstructor`. If you need `equals`/`hashCode`, base them on the id or a business key.
- **Let the database generate ids** (`@GeneratedValue`). Don't set ids by hand.
- **Annotations come from `jakarta.persistence.*`**, not `javax.persistence.*`.
- **Relationships:** use `@ManyToOne(fetch = LAZY)` on the owning side. Use `@OneToMany(mappedBy = ...)` only when you need to navigate the collection. Leave relationship fields out of `toString`. To stop infinite JSON recursion, use DTOs, or `@JsonIgnore`/`@JsonManagedReference` as a stopgap.
- **Consider DTOs** (`dto/` package) once entities have relationships or internal fields, so the API contract stays separate from the database schema.
- **Put validation on DTOs or entities** with Jakarta Bean Validation (`@NotBlank`, `@Email`) once `spring-boot-starter-validation` is added, and use `@Valid` in controllers.

## Adding a new model / entity

1. Create the class (in `model/` once packages are split, see [architecture.md](architecture.md)).
2. Add a section here with a field table.
3. If it's an entity, add its repository to [repository.md](repository.md).
