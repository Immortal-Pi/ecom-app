# API Reference

Every HTTP endpoint the application exposes. The base URL when running locally is `http://localhost:8080`.

**Status: current.** Keep this file in sync with the controllers ([controller.md](controller.md)).

## Users — `/api/users`

The `User` JSON shape (see [model.md](model.md)):

```json
{ "id": 1, "firstName": "Ada", "lastName": "Lovelace" }
```

| Method | Path | Request body | Success | Failure |
|---|---|---|---|---|
| GET | `/api/users` | — | `200` JSON array of users | — |
| GET | `/api/users/{id}` | — | `200` user JSON | `404` empty body |
| POST | `/api/users` | user JSON (`id` is ignored) | `200` text `User added successfully` | — |
| PUT | `/api/users/{id}` | user JSON (only `firstName` and `lastName` are used) | `200` text `User updated successfully` | `404` empty body |

### GET /api/users

```bash
curl http://localhost:8080/api/users
```
```json
[{ "id": 1, "firstName": "Ada", "lastName": "Lovelace" }]
```

### GET /api/users/{id}

```bash
curl -i http://localhost:8080/api/users/1
```
Returns `200` with the user, or `404 Not Found` if no user has that id.

### POST /api/users

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Ada","lastName":"Lovelace"}'
```
```
User added successfully
```
The server assigns ids in order starting at `1`. The response doesn't include the created user or its id.

### PUT /api/users/{id}

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Augusta","lastName":"King"}'
```
```
User updated successfully
```
Returns `404` if no user has that id.

## Notes

- Data is in memory only and resets when the app restarts, until JPA is added ([repository.md](repository.md)).
- No authentication, input validation or standard error body yet.
