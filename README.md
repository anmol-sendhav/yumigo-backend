# Yumigo Backend

A food ordering and delivery backend built with Java and Spring Boot, structured the way
production services are: layered architecture, DTOs, centralized error handling,
versioned database migrations, and stateless JWT security.

> Status: in active development. The API is being built feature by feature.

## Implemented
- Registration and login with BCrypt password hashing
- JWT access tokens and single-use, rotating refresh tokens with reuse detection
- Role-based access control (Customer, Restaurant Owner, Admin)
- Restaurant management with search, filters, and pagination *(in progress)*
- Consistent JSON error responses for validation, authentication, and server errors

## Planned
- Menu management, cart, and order lifecycle with status transitions
- Reviews, Redis caching, payments, delivery assignment
- Unit and integration tests, Swagger/OpenAPI documentation

## Tech Stack
Java <your version>, Spring Boot 3, Spring Security, Spring Data JPA (Hibernate),
MySQL 8, Flyway, Maven, Lombok

## API Overview
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/auth/register` | Public |
| POST | `/api/v1/auth/login` | Public |
| POST | `/api/v1/auth/refresh` | Public |
| POST | `/api/v1/auth/logout` | Authenticated |
| GET | `/api/v1/users/me` | Authenticated |
| POST | `/api/v1/restaurants` | Restaurant Owner |
| GET | `/api/v1/restaurants` | Public |
| GET | `/api/v1/restaurants/{id}` | Public |
| GET | `/api/v1/restaurants/my` | Restaurant Owner |
| PATCH | `/api/v1/restaurants/{id}/status` | Admin |

## Project Structure
```
com.yumigo
├── config        application and security configuration
├── controller    HTTP layer (no business logic)
├── service       business rules and transactions
├── repository    database access (Spring Data JPA)
├── entity        JPA entities
├── dto           request and response objects
├── mapper        entity <-> DTO conversion
├── security      JWT filter, user details, error handlers
└── exception     custom exceptions and global handler
```

## Run Locally
1. Install Java and MySQL 8.
2. Create the database:
   ```sql
   CREATE DATABASE yumigo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. Set environment variables:
   - `DB_USERNAME` (default `root`)
   - `DB_PASSWORD`
   - `JWT_SECRET` (a random string of at least 32 characters)
4. Start the app: `./mvnw spring-boot:run`

Flyway creates all tables automatically on first start.

## Design Notes
- **Flyway + `ddl-auto: validate`:** the schema is versioned in SQL files, and Hibernate only verifies it.
- **Refresh token rotation:** each refresh token works once. Reusing an old one revokes all of that user's tokens.
- **Entities never leave the service layer:** controllers only see DTOs.

## Author
<anmol-sendhav> | <anmolsendhav54@gmail.com>
