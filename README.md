# OpenStream API

Backend for **OpenStream**, a public microblogging platform. A Spring Boot REST API backed by PostgreSQL, containerised with Docker and deployed to Render.

**Live API:** https://openstream-api.onrender.com
**Frontend repo:** [anutej-kardele/OpenStream](https://github.com/anutej-kardele/OpenStream)

> The API runs on a free Render instance, which spins down after inactivity. The first request after an idle period takes up to 60 seconds while the container cold-starts.

---

## What it does

Users publish short posts, follow each other, and read a chronological feed assembled from the people they follow plus their own posts. The interesting problem is the feed: a single query that joins across the follow graph and returns posts in one round trip.

---

## Stack

| Layer | Choice |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.1 (Spring Web, Spring Data JPA) |
| ORM | Hibernate 7.4 |
| Database | PostgreSQL 18 (Neon, serverless) |
| Testing | JUnit 5, Mockito |
| Container | Docker (multi-stage build) |
| Hosting | Render |

---

## Architecture

```
Controller  →  Service  →  Repository  →  Hibernate  →  PostgreSQL
    ↓             ↓
   DTOs      business rules
```

Layered so each part has one job:

- **Controllers** map HTTP to method calls and nothing else. No business logic.
- **Services** hold every rule — validation, authorisation checks, transaction boundaries — and return DTOs rather than entities.
- **Repositories** are Spring Data interfaces; derived query methods for the simple cases, JPQL for the feed.
- **DTOs** keep entities out of the API contract. Building them inside the transaction also avoids `LazyInitializationException` during serialisation.

---

## API

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/users` | Create a user |
| `GET` | `/api/users/{handle}` | Look up a user by handle |
| `POST` | `/api/posts` | Publish a post |
| `GET` | `/api/posts/feed/{userId}` | Chronological feed |
| `POST` | `/api/users/{followerId}/follow/{followedId}` | Follow |
| `DELETE` | `/api/users/{followerId}/follow/{followedId}` | Unfollow |
| `GET` | `/api/users/{userId}/following` | Users this user follows |
| `GET` | `/api/users/{userId}/followers` | Users following this user |
| `GET` | `/api/users/{userId}/follow-counts` | Both counts in one call |

Validation failures return `400` with a JSON body, handled centrally by a `@RestControllerAdvice` rather than try/catch in each controller.

```bash
curl -X POST https://openstream-api.onrender.com/api/users \
  -H "Content-Type: application/json" \
  -d '{"username": "Ada Lovelace", "handle": "ada"}'
```

---

## Testing

14 unit tests across the service layer, run with `./mvnw test`.

Services are tested in isolation — repositories are replaced with Mockito mocks, so the suite runs in well under a second with no database. Constructor injection is what makes this possible.

Coverage focuses on the rules rather than the framework:

- Content validation (empty, whitespace-only, over 280 characters)
- Handle normalisation and format rules
- Duplicate handles and duplicate follows
- Self-follow rejection
- Error paths for missing users
- Side effects verified with `verify()` where methods return `void`

---

## Running locally

**Requirements:** JDK 21+, a PostgreSQL database (Neon's free tier works well).

Credentials are read from the environment, never committed. Copy `.env.example` and fill it in:

```bash
export DB_URL="jdbc:postgresql://<host>/<database>?sslmode=require"
export DB_USERNAME="<user>"
export DB_PASSWORD="<password>"
```

Then:

```bash
source .env
./mvnw spring-boot:run
```

The app starts on port 8080. Hibernate creates the schema from the entity classes on first run.

---

## Deployment

Docker multi-stage build: the first stage compiles with a full JDK, the second copies only the resulting jar onto a JRE base image. Dependency resolution is a separate layer from the source copy, so code-only changes rebuild in seconds.

Render builds from `main` on push. `server.port=${PORT:8080}` picks up the platform-assigned port while keeping 8080 as the local default.

---

## Notes and next steps

Honest about what this is and isn't:

- **No authentication yet.** The API trusts the user ids it receives. Spring Security with JWT is the next substantial piece.
- **Schema is managed by `ddl-auto=update`,** which is fine for development but can't handle renames or drops. Flyway migrations are the production answer.
- **The feed isn't paginated.** Returning `Page<Post>` with a `Pageable` parameter is a small change and the right one before this sees real volume.
- **Planned:** a Python/FastAPI microservice running a PyTorch classifier, called from `POST /api/posts` to reject harmful content before it reaches the database.
