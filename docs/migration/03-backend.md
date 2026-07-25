# 03 — Backend Migration: Java EE Monolith → Spring Boot Microservices

## Goal

Replace the Java EE monolith with a set of Spring Boot microservices using the **Strangler Fig pattern** — no big-bang rewrite, no downtime, same API surface exposed to the frontend throughout.

---

## Strangler Fig Pattern Explained

```
                    ┌──────────────────┐
Frontend ──────────►│   API Gateway    │
                    └────────┬─────────┘
                             │
              ┌──────────────┼──────────────────┐
              │              │                  │
              ▼              ▼                  ▼
       [Java EE app]   [Spring Service A]  [Spring Service B]
       (still live)    (new, owns /api/a)  (new, owns /api/b)
```

The gateway routes requests to the new services as they go live. The old Java EE app handles anything not yet migrated. When all routes are covered, the Java EE app is retired.

---

## Proposed Microservice Boundaries

Based on a typical anime/media platform, the domain splits naturally into:

| Service | Responsibility | Owns |
|---------|---------------|------|
| `auth-service` | Login, JWT, sessions | User credentials, tokens |
| `user-service` | Profiles, preferences, follows | User data |
| `catalog-service` | Anime/game catalogue, search | Media metadata |
| `list-service` | User watchlists, favourites | User-media relations |
| `review-service` | Ratings, reviews, comments | Review data |
| `notification-service` | Push/email notifications | Notification queue |
| `media-service` | Images, assets (if self-hosted) | Binary storage (Cloud Storage) |

> **Rule:** No service calls another service's database. Cross-service data is requested via internal HTTP/gRPC or event-driven messaging (Pub/Sub).

---

## Technology Stack

| Concern | Choice | Reason |
|---------|--------|--------|
| Framework | Spring Boot 3.x | Modern, reactive, cloud-native |
| Language | Java 21 (LTS) | Virtual threads, records, pattern matching |
| API style | REST + OpenAPI 3 | Backward-compatible with existing frontend |
| Service discovery | Cloud Run (serverless) or GKE | Managed, scales to zero |
| API Gateway | Cloud Endpoints or Spring Cloud Gateway | Routes to correct service |
| Inter-service comms | Google Cloud Pub/Sub (async) + REST (sync) | |
| Config management | Google Secret Manager + Spring Cloud Config | |
| Observability | Cloud Trace + Micrometer + structured logging | |
| Contract testing | Spring Cloud Contract | Ensures API shape doesn't drift |

---

## Step-by-Step Migration

### Step 1 — Install API Gateway and proxy everything to Java EE

Before writing a single line of Spring code, put a gateway in front of the existing app. All traffic still goes to Java EE — nothing changes for the frontend.

```yaml
# Cloud Endpoints or Spring Cloud Gateway config
routes:
  - id: legacy
    uri: http://java-ee-app:8080
    predicates:
      - Path=/**
```

### Step 2 — Define API contracts (OpenAPI)

Document every existing endpoint the frontend calls. This becomes the contract each new service must honour.

```yaml
# openapi.yaml (example)
paths:
  /api/animes:
    get:
      summary: List animes
      responses:
        '200':
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Anime'
```

Generate Spring Cloud Contract stubs from these specs. Every new service must pass contract tests before traffic is shifted to it.

### Step 3 — Build services one at a time (start with auth)

Start with the service that has the fewest dependencies. `auth-service` is usually the best first service because it's largely self-contained.

Each service follows this structure:

```
auth-service/
  src/main/java/ma/otaku/auth/
    AuthController.java       ← same REST endpoints as Java EE
    AuthService.java          ← business logic
    AuthRepository.java       ← Firestore access (see DB migration doc)
    model/
      User.java
      TokenResponse.java
  src/test/
    AuthControllerTest.java
    AuthContractTest.java     ← Spring Cloud Contract verification
  Dockerfile
  pom.xml
```

**Spring Boot service template:**

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
```

### Step 4 — Shift traffic to each new service

Once a service passes contract tests and load tests, update the gateway to route its paths to the new service:

```yaml
routes:
  - id: auth-service
    uri: http://auth-service:8080
    predicates:
      - Path=/api/auth/**
  - id: legacy          # everything else still goes to Java EE
    uri: http://java-ee-app:8080
    predicates:
      - Path=/**
```

### Step 5 — Repeat for each service

Order of recommended migration (least to most dependent):

1. `auth-service`
2. `catalog-service`
3. `user-service`
4. `list-service`
5. `review-service`
6. `notification-service`
7. `media-service`

### Step 6 — Retire Java EE

Once all gateway routes point to Spring services and no traffic reaches Java EE, take it offline. Remove the legacy route from the gateway.

---

## Spring Boot Service Template (pom.xml)

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>spring-cloud-gcp-starter-firestore</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-contract-verifier</artifactId>
    <scope>test</scope>
  </dependency>
</dependencies>
```

---

## API Versioning Strategy

The frontend calls `/api/...` — these stay frozen during migration.

If a breaking change is ever needed after the frontend migration:
- Introduce `/api/v2/...` alongside `/api/v1/...`
- Deprecate v1 with a sunset header
- Migrate frontend to v2 in the next sprint
- Remove v1 after confirmed cutover

---

## Observability

Every service emits:
- **Structured JSON logs** → Cloud Logging
- **Traces** via Micrometer + Cloud Trace
- **Metrics** → Cloud Monitoring (request rate, error rate, latency p50/p95/p99)
- **Health endpoints** → `/actuator/health` for Cloud Run health checks

---

## Checklist

- [ ] Deploy API Gateway in front of Java EE app
- [ ] Document all existing API endpoints as OpenAPI spec
- [ ] Generate Spring Cloud Contract stubs from OpenAPI
- [ ] Scaffold `auth-service` and pass contract tests
- [ ] Shift `/api/auth/**` traffic to `auth-service`
- [ ] Scaffold `catalog-service` and pass contract tests
- [ ] Shift `/api/catalog/**` (or equivalent) traffic
- [ ] Scaffold `user-service`, `list-service`, `review-service`
- [ ] Shift remaining traffic
- [ ] All services instrumented with logging + tracing
- [ ] Java EE app receives zero traffic
- [ ] Java EE app decommissioned

---

## References

- [Spring Boot 3 Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Cloud GCP Firestore](https://googlecloudplatform.github.io/spring-cloud-gcp/docs/current/reference/html/)
- [Spring Cloud Contract](https://spring.io/projects/spring-cloud-contract)
- [Strangler Fig Pattern](https://martinfowler.com/bliki/StranglerFigApplication.html)
