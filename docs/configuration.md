# Configuration & Build

The stack, build setup, runtime properties and planned database configuration.

## Stack

**Status: current**

| Component | Version / detail |
|---|---|
| Java | 27 (`<java.version>` in `pom.xml`) |
| Spring Boot | 4.1.2 (`spring-boot-starter-parent`) |
| Web | `spring-boot-starter-webmvc` (Spring MVC, embedded Tomcat, Jackson) |
| Lombok | `org.projectlombok:lombok` (optional dependency) |
| Tests | `spring-boot-starter-webmvc-test` |
| Build | Maven, through the wrapper (pins Maven 3.10.0) |
| Persistence | none yet (see [Database & JPA](#database--jpa)) |

## Commands

Use `./mvnw` in Git Bash and `.\mvnw.cmd` in PowerShell.

| Task | Command |
|---|---|
| Build | `./mvnw clean package` |
| Run (port 8080) | `./mvnw spring-boot:run` |
| All tests | `./mvnw test` |
| One test class | `./mvnw test -Dtest=EcomApplicationTests` |
| One test method | `./mvnw test -Dtest=EcomApplicationTests#contextLoads` |

## `application.properties`

`../src/main/resources/application.yaml`, currently:

```properties
spring.application.name=ecom-application
```

When you add a property, document it in this section.

## POM notes

- **Lombok annotation processor:** `maven-compiler-plugin` lists Lombok under `annotationProcessorPaths` in both the `default-compile` and `default-testCompile` executions. Add any other processor (MapStruct, `hibernate-jpamodelgen`) to the same lists, or Lombok stops running.
- **Empty `<licenses>`, `<developers>`, `<scm>`, `<name/>`, `<description/>`, `<url/>`:** these are intentional. They stop the project inheriting those values from the Spring Boot parent (see `HELP.md`). Don't remove them.

## Database & JPA

**Status: planned**

### Dependencies to add to `pom.xml`

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- pick ONE driver -->
<dependency>                       <!-- in-memory, good for dev/tests -->
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
<!-- or org.postgresql:postgresql / com.mysql:mysql-connector-j (scope runtime) -->
```

Spring Boot manages the versions, so leave out `<version>`.

### Properties

```properties
# H2 (in-memory)
spring.datasource.url=jdbc:h2:mem:ecom
spring.h2.console.enabled=true

# PostgreSQL example
# spring.datasource.url=jdbc:postgresql://localhost:5432/ecom
# spring.datasource.username=ecom
# spring.datasource.password=${DB_PASSWORD}

# Schema handling: create-drop (dev/tests), update (local only), validate (prod, with Flyway/Liquibase)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

- Keep passwords out of the repo. Read them from environment variables (`${DB_PASSWORD}`).
- For production schemas, use a migration tool (Flyway or Liquibase) with `ddl-auto=validate`, and document it here.

Once this is in place, change the Stack table and this section to **current**, and update the Stack line in [../CLAUDE.md](../CLAUDE.md).
