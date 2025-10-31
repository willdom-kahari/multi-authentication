# Multi-Authentication (Spring Boot)

A minimal Spring Boot 3 application demonstrating multiple authentication mechanisms:

- Username/password authentication (via Spring Security `AuthenticationManager`)
- JWT-based auth for protected endpoints (as an OAuth2 Resource Server)
- Basic Auth enabled (primarily for Swagger testing convenience)
- OpenAPI/Swagger UI via springdoc

The app exposes a token issuance endpoint and two role-protected endpoints to illustrate role-based access control (RBAC).


## Stack
- Language: Java 17
- Frameworks: Spring Boot 3.5.x, Spring Security, OAuth2 Resource Server, springdoc-openapi
- Build/Package manager: Gradle (Wrapper included)
- OpenAPI UI: springdoc-openapi-starter-webmvc-ui


## Requirements
- Java 17 (JDK 17)
- Internet access to download dependencies from Maven Central
- Optional: cURL or an API client (e.g., Postman)

You do not need to install Gradle; the project uses the Gradle Wrapper (`./gradlew`).


## Getting Started

### Clone
```bash
git clone <your-fork-or-repo-url>
cd multi-authentication
```

### Build
```bash
./gradlew clean build
```

### Run (dev)
```bash
./gradlew bootRun
```

By default, the application starts on port `8080`.

### Run (jar)
```bash
./gradlew bootJar
java -jar build/libs/multi-authentication-0.0.1-SNAPSHOT.jar
```


## Application Entry Point
- `src/main/java/com/waduclay/multiauthentication/MultiAuthenticationApplication.java`


## Endpoints (Overview)
- Public (no auth required):
  - `POST /auth` — Exchange username/password for a JWT access token

- Protected (JWT Bearer required):
  - `GET /user` — Requires role `ROLE_USER`
  - `GET /admin` — Requires role `ROLE_ADMIN`

- OpenAPI/Swagger:
  - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
  - OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Security configuration permits access to `/auth`, `/swagger-ui/**`, `/v3/api-docs/**` without authentication; all other endpoints require authentication.


## Default Users (In-Memory)
Configured in `application.properties` and loaded via `SecurityConfig`:
- USER: `user-one` / `password` — role `ROLE_USER`
- ADMIN: `user-two` / `password` — role `ROLE_ADMIN`

You can override these via environment variables (see Environment Configuration below).


## Authentication Flow
1. Call `POST /auth` with JSON body:
   ```json
   { "username": "user-one", "password": "password" }
   ```
2. You’ll receive a response like:
   ```json
   { "success": true, "token": "<JWT_TOKEN>" }
   ```
3. Use the token to access protected endpoints with header:
   ```
   Authorization: Bearer <JWT_TOKEN>
   ```

Example cURL:
```bash
# 1) Get token
curl -s -X POST http://localhost:8080/auth \
  -H 'Content-Type: application/json' \
  -d '{"username":"user-one","password":"password"}'

# 2) Call /user with the returned token
curl -s http://localhost:8080/user \
  -H 'Authorization: Bearer <JWT_TOKEN>'

# 3) Call /admin with ADMIN user
curl -s -X POST http://localhost:8080/auth \
  -H 'Content-Type: application/json' \
  -d '{"username":"user-two","password":"password"}'

curl -s http://localhost:8080/admin \
  -H 'Authorization: Bearer <JWT_TOKEN>'
```


## Scripts (Gradle Wrapper)
Common tasks:
- `./gradlew bootRun` — run the application in dev mode
- `./gradlew build` — build the project (runs tests)
- `./gradlew test` — run tests
- `./gradlew clean` — clean build outputs
- `./gradlew bootJar` — build an executable JAR


## Environment Configuration
Configuration is provided via `src/main/resources/application.properties` and can be overridden with environment variables (Spring Boot’s relaxed binding). Defaults in this repo:

```properties
# Application
spring.application.name=multi-authentication

# In-memory users (SecurityConfig)
user.one.name=user-one
user.two.name=user-two
user.one.password=password
user.two.password=password

# JWT HMAC secret (used by NimbusJwtEncoder/Decoder)
jwt.secret=aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d
```

To override with environment variables, convert property names to upper snake case:
- `user.one.name` -> `USER_ONE_NAME`
- `user.two.name` -> `USER_TWO_NAME`
- `user.one.password` -> `USER_ONE_PASSWORD`
- `user.two.password` -> `USER_TWO_PASSWORD`
- `jwt.secret` -> `JWT_SECRET`

Example (Unix):
```bash
export USER_ONE_NAME=my-user
export USER_ONE_PASSWORD=my-pass
export JWT_SECRET=change-me-please
./gradlew bootRun
```

Security notes:
- Always set a strong `JWT_SECRET` for any non-local usage.
- Tokens expire after 1 day by default (see `TokenService`).


## Tests
Run the test suite:
```bash
./gradlew test
```

The project includes a basic Spring Boot context test:
- `src/test/java/com/waduclay/multiauthentication/MultiAuthenticationApplicationTests.java`


## Project Structure
```
multi-authentication/
├─ build.gradle
├─ settings.gradle
├─ gradle/wrapper/
├─ gradlew, gradlew.bat
├─ src/
│  ├─ main/java/com/waduclay/multiauthentication/
│  │  ├─ MultiAuthenticationApplication.java         # Entry point
│  │  ├─ config/
│  │  │  ├─ SecurityConfig.java                     # HTTP security, resource server, in-memory users
│  │  │  ├─ JwtConfiguration.java                    # JwtEncoder/Decoder (Nimbus, HS256)
│  │  │  ├─ JwtAuthoritiesMapper.java                # Maps JWT claims to Spring authorities
│  │  │  ├─ OpenApiConfig.java                       # OpenAPI + security schemes
│  │  │  └─ UserConfigurationProperties.java         # Config props for users
│  │  ├─ controller/
│  │  │  ├─ AuthController.java                      # POST /auth
│  │  │  └─ AppController.java                       # GET /user, GET /admin
│  │  ├─ dto/
│  │  │  ├─ AuthenticationRequest.java
│  │  │  ├─ AuthenticationResponse.java
│  │  │  └─ GenericResponse.java
│  │  └─ service/
│  │     ├─ AuthenticationService.java               # Auth via AuthenticationManager, returns JWT
│  │     └─ TokenService.java                         # JWT creation (issuer, expiry, authorities)
│  └─ main/resources/
│     └─ application.properties
└─ src/test/java/com/waduclay/multiauthentication/
   └─ MultiAuthenticationApplicationTests.java
```


## Configuration/Design Notes
- The app is stateless (`SessionCreationPolicy.STATELESS`).
- `/auth` is open; all other endpoints require auth.
- JWT uses HS256 (HMAC) with a secret key. Claims include `authorities` and `sub`.
- `JwtAuthoritiesMapper` ensures authorities have the `ROLE_` prefix.
- Springdoc exposes OpenAPI UI with both Bearer and Basic security schemes for testing.
