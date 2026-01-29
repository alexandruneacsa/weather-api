# Weather Demo — Roadmap & Tasks

Acesta este un README pragmatic cu task-uri concrete, idei de îmbunătățire și integrări open-source pe care le poți implementa în proiectul `demo` (Spring Boot + Liquibase + PostgreSQL).

Scop: oferă o listă prioritizată de lucruri de făcut — de la quick fixes și automatisări până la integrare observability, CI/CD și securitate enterprise.

Rezumat rapid (plan):
- Fixuri imediate pentru local dev (DB mapping, security, migrations) — 1–2 zile
- Testare + CI + containerizare — 1–2 zile
- Observability & tracing (Prometheus / Grafana / Jaeger) — 1–2 zile
- Integrări enterprise open-source (Keycloak, Kafka, MinIO, ELK) — 2–5 zile, după priorități

Check-listă (pe niveluri)
- [ ] Short-term (quick wins)
  - [ ] Curățenie: verifică mapping-urile entităților vs Liquibase (tabel `weather`) — DONE/verify
  - [ ] Dezactivează/permite securitate locală (ex: dev profile fără auth) — DONE/verify
  - [ ] Adaugă Swagger/OpenAPI (springdoc-openapi) pentru API docs
  - [ ] Adaugă unit/integration tests pentru controller + repository (folosind Testcontainers pentru Postgres)
- [ ] Medium-term
  - [ ] Dockerize (app + local Postgres + Adminer/pgAdmin)
  - [ ] CI (GitHub Actions): build, test, image build, push to registry
  - [ ] Database migrations: rulează & verifică Liquibase în pipeline
  - [ ] Add healthchecks + readiness/liveness
- [ ] Long-term / Integrări
  - [ ] Metrics + Dashboard: Prometheus + Grafana
  - [ ] Distributed Tracing: Jaeger / OpenTelemetry
  - [ ] Auth: Keycloak (OpenID Connect) + RBAC integration
  - [ ] Object storage: MinIO (S3-compatible) pentru uploads/backups
  - [ ] Message-broker: Kafka sau RabbitMQ pentru event-driven flows
  - [ ] Logging & Search: ELK stack (Elasticsearch + Logstash + Kibana)
  - [ ] Secrets: Vault (HashiCorp) sau Kubernetes secrets

Quick start (dev)

Prerechizite
- Java toolchain (project folosește toolchain config)
- Gradle wrapper (folosește `./gradlew`)
- Postgres local (sau Folosește Testcontainers pentru test automat)

Comenzi utile

- Build & test (local):

```bash
./gradlew clean test
```

- Start aplicație cu profile `local` (folosește configurația din `src/main/resources/config/application-local.yaml`):

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

- Rulează doar unit tests (faster):

```bash
./gradlew test --tests "com.weather.demo.*"
```

API endpoints (curent)
- GET /api/weather — listă (returnează JSON)
- GET /api/weather/{id} — detaliu

Note: controller-ul actual returnează DTO-urile direct; verifică validările/serialization pentru câmpuri null.

Concrete short-term tasks (high ROI)

1) Stabilize DB mapping & migrations (PR A)
- Task: aliniază entitățile JPA cu changelog-urile Liquibase (tabel `weather` există în changelog)
- Why: evită runtime errors "relation does not exist" sau mismatch-uri de coloană
- Done checklist:
  - mapping @Table(name = "weather") pe entitate
  - tipuri Java (Double/Integer/Instant) compatibile cu SQL
  - rulează `./gradlew clean test` (Testcontainers va aplica migrations)

2) Swagger / OpenAPI (PR B)
- Add dependency: `org.springdoc:springdoc-openapi-starter-webmvc-ui`
- Expose `/swagger-ui.html` sau `/swagger-ui/index.html` pentru explorarea API
- Tasks:
  - adaugă maven/gradle depency
  - configurează minimal descriptions pentru controller methods
  - verify UI în browser

3) Tests & Quality (PR C)
- Add tests for controller (MockMvc) and repository (DataJpaTest + Testcontainers)
- Add a failing-case test (empty DB) and success-case test (with inserted rows)
- Hook tests into CI

4) Disable security in dev (or provide a dev-only insecure profile)
- Two options:
  - Keep security on, but provide a `dev` profile that disables it (recommended for safety)
  - Remove `spring-boot-starter-security` from dependencies for local experiments (you already did)
- Prefer: keep security in prod, disable in local via property e.g. `spring.security.enabled=false` (if implemented)

Medium-term tasks (project hygiene)

5) Docker Compose for local stack
- services:
  - app (built from Dockerfile)
  - postgres (official image)
  - pgadmin OR adminer
  - optional: prometheus + grafana for metrics
- Add `.env` for credentials and compose overrides

6) CI pipeline
- Steps: checkout, setup JDK, gradle build & tests, build image, push to registry (GHCR/DockerHub)
- Add Liquibase check step (validate migrations)
- Add pipeline matrix (jdk versions if needed)

7) Observability
- Prometheus: expose metrics via Micrometer (add dependency `io.micrometer:micrometer-registry-prometheus`)
- Grafana: dashboard for CPU/Memory + JVM + app metrics (http.server.requests, datasource metrics)
- Jaeger / OpenTelemetry: tracing for request flows (spring-cloud-sleuth is deprecated — use opentelemetry instrumentation)

Advanced / Integrations (open-source)

- Keycloak (OIDC)
  - Use for SSO, user management, roles; integrate via `spring-boot-starter-oauth2-client`/resource-server
  - Flow: protect endpoints, map roles to authorities
- Kafka (Apache Kafka)
  - For event-driven architecture: publish weather events (changes) to topics
  - Use `spring-kafka` and add schema registry if needed
- MinIO
  - For object storage (backups, attachments). S3-compatible, easy to run locally
- ELK stack (Elasticsearch + Filebeat/Kibana)
  - Centralized logs + search + dashboards
- Vault (HashiCorp Vault)
  - Manage secrets safely in CI / production

Small developer tasks (good first issues)
- [ ] Add API contract tests (use `rest-assured` or `spring-test` with MockMvc)
- [ ] Add validation for DTOs (Hibernate Validator / javax.validation)
- [ ] Implement pagination for GET /api/weather
- [ ] Implement filtering by city (repository method `findByCity` already present) and add controller endpoint param
- [ ] Add API rate-limiting (bucket4j or Spring RateLimiter)
- [ ] Add exception handler (`@ControllerAdvice`) to map exceptions to problem+status

Estimări & Prioritizare
- Immediate fixes & tests: 1-2 days
- Docker+CI: 1-2 days
- Observability & tracing: 1-3 days
- Keycloak + message bus: 2-5 days (more integration work)

Acceptance criteria (example for a task)
- Task: "Add OpenAPI and Swagger UI"
  - GIVEN project builds
  - WHEN I start the app with `local` profile
  - THEN `/swagger-ui/index.html` is reachable and documents `/api/weather` endpoints
  - AND CI validates that the dependency is present

Contribution workflow suggestions
- Branch naming: `feature/<short-desc>` or `fix/<short-desc>`
- PR template: summary, what changed, how to test, screenshots (if UI)
- Labels: `good-first-issue`, `bug`, `enhancement`, `security`, `infra`

Useful dependencies to add (gradle coord)
- OpenAPI: `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0` (check latest)
- Prometheus: `io.micrometer:micrometer-registry-prometheus`
- OpenTelemetry / Jaeger: `io.opentelemetry:opentelemetry-exporter-jaeger` + starter integrations
- Keycloak: `org.keycloak:keycloak-spring-boot-starter` or use Spring Security OAuth/Resource Server
- Kafka: `org.springframework.kafka:spring-kafka`
- MinIO client (java): `io.minio:minio`

How I can help next
- Vrei să-ți creez PR-uri pentru 1) OpenAPI 2) Docker Compose 3) GitHub Actions? Spune care 1 sau 2 task-uri vrei să le automatizez și le implementez.

---

Dacă vrei, pot adăuga acest README în repo (creat deja), sau îl pot extinde cu:
- un exemplu de `docker-compose.yaml` pentru dezvoltare (app + postgres + prometheus + grafana),
- un workflow GitHub Actions minimal,
- un `swagger config` și un PR care adaugă `springdoc`.

Spune-mi ce vrei să automatizez pentru tine (ex: adaug docker-compose + start scripts + healthcheck).
