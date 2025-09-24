# Chatop Backend with JWT Auth (generated)

This project is a generated scaffold implementing the following requested features:
- Spring Boot 3.5.5, Java 17, Maven
- MySQL-ready configuration (edit credentials in application.properties)
- JWT authentication (login/register/me)
- Entities: USERS, RENTALS, MESSAGES (mapped to DB)
- File upload for rental pictures (stored under `uploads/`)
- Swagger UI: `/swagger-ui.html`
- Example controllers for Rentals and Messages

## How to run locally
1. Ensure you have a MySQL server running and a database `chatop` created:
   ```sql
   CREATE DATABASE chatop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Update credentials in `src/main/resources/application.properties` if needed.
3. Run:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Swagger UI: http://localhost:8080/swagger-ui.html
5. Important: change `chatop.jwt.secret` value to a secure random secret in production.

## Notes
- JWT secret currently in properties for demo. For production, store it securely (env var or vault).
- I used `spring.jpa.hibernate.ddl-auto=update` for convenience; prefer Flyway for migrations.
