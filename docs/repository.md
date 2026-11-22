# Repository Layer

**Status: planned, not yet implemented.** For now `UserService` stores data in an in-memory list (see [service.md](service.md)).

The repository layer is the only layer that talks to the database. It uses **Spring Data JPA**: you declare interfaces, Spring generates the implementations at runtime, and the code works with entities (`User`) instead of SQL.

## Setup

Dependencies and `application.properties` settings are in [configuration.md](configuration.md#database--jpa).

## Conventions

- One repository per aggregate/entity, named `<Entity>Repository`, in the `repository/` package.
- Make it an interface extending `JpaRepository<Entity, IdType>`. No class or `@Repository` annotation is needed.
- Only services call repositories. Controllers never do.
- Pick query styles in this order:
  1. **Built-in methods** (`findById`, `save`, ...)
  2. **Derived query methods**: Spring builds the query from the method name
  3. **`@Query` with JPQL**: refers to entity and field names, not tables and columns
  4. **Native SQL** (`@Query(nativeQuery = true)`): only when JPQL can't express the query, and document why here
- Transactions belong in the service layer (`@Transactional` on service methods), not here.

## Example: `UserRepository`

```java
package com.app.ecom.repository;

public interface UserRepository extends JpaRepository<User, Long> {
    // derived queries: no implementation needed
    List<User> findByLastName(String lastName);
    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);
    boolean existsByLastName(String lastName);

    // JPQL, written against the entity
    @Query("select u from User u where lower(u.firstName) like lower(concat('%', :q, '%'))")
    List<User> searchByFirstName(@Param("q") String q);
}
```

## Built-in methods from `JpaRepository`

| Method | Purpose | Replaces in current `UserService` |
|---|---|---|
| `findAll()` | All rows | returning `userList` |
| `findById(id)` | `Optional<T>` by primary key | the stream filter in `fetchUser` |
| `save(entity)` | Insert (no id) or update (has id). Returns the managed entity with its generated id | `nextId++` + `userList.add` |
| `saveAll(list)` | Insert or update many | — |
| `existsById(id)` | Existence check | — |
| `deleteById(id)` | Delete by primary key | — (no delete endpoint yet) |
| `count()` | Row count | — |
| `findAll(Pageable)` / `findAll(Sort)` | Paging and sorting | — |

## Derived query keywords (cheat sheet)

`findBy`, `existsBy`, `countBy`, `deleteBy`, combined with `And`, `Or`, `Between`, `LessThan`, `GreaterThan`, `Like`, `Containing`, `StartingWith`, `IgnoreCase`, `In`, `IsNull`, `OrderBy<Field>Asc|Desc`. Example: `findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName)`.

## Once implemented

Replace the status line at the top with **Status: current**, list each real repository and its custom methods, and update [service.md](service.md), [architecture.md](architecture.md) and the overview in [../CLAUDE.md](../CLAUDE.md) and [../README.md](../README.md).
