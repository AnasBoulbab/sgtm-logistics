# SGTM Logistics

SGTM Logistics is a Spring Boot service that exposes APIs for managing vehicles, construction sites, and daily reports while syncing data from external GPS providers.

## Running the API locally

1. **Prerequisites:** JDK 17+ and Maven (or the included `./api/mvnw` wrapper).
2. **Start the service:**
   ```bash
   cd api
   ./mvnw spring-boot:run
   ```
   The dev profile is active by default and starts the API on port `9898`.

## Configuration

The service reads configuration from `api/src/main/resources/application-<profile>.yaml`. The default profile is `dev`.

### Database and Flyway
- `spring.datasource.*`: JDBC settings for PostgreSQL.
- `spring.jpa.hibernate.ddl-auto`: set to `validate` to rely on migrations.
- `spring.flyway.locations`: migration scripts grouped by profile under `classpath:db/migration/<profile>`.

### GPS provider selection

GPS integrations are enabled through the `gps-provider` properties. Only one provider is active at a time using the `gps-provider.name` flag, which toggles the corresponding `@ConditionalOnProperty` services.

**Supported providers:**
- `gpswox` (enabled by default in `application-dev.yaml`)
- `traccar` (commented example in `application-dev.yaml`)

**Shared properties**
```yaml
gps-provider:
  name: gpswox | traccar   # Selects which GPS integration to load
  base-url: https://provider-host/api
  email: provider-user@example.com
  password: provider-password
  user-api-hash: optional-api-hash-for-gpswox
```

**Example: enable GPSWOX (default in dev)**
```yaml
gps-provider:
  name: gpswox
  base-url: https://app.aza-gps.ma/api
  email: user@example.com
  password: <password>
  user-api-hash: <api-hash-if-required>
```

**Example: switch to Traccar**
```yaml
gps-provider:
  name: traccar
  base-url: http://traccar-host:8082/api
  email: user@example.com
  password: <password>
```

When `gps-provider.name` is changed, restart the application so Spring can load the appropriate clients (`GpswoxClient`/`TraccarClient`) and services (`GpswoxService`/`TraccarService`).

### Synchronization schedule

Cron expressions for background synchronization are controlled by `synchronization.cron.*` in the same YAML file:
- `devices`: when remote devices are synchronized locally.
- `day-report`: when day reports are synchronized.

## API documentation

OpenAPI/Swagger is available once the service is running:
- Swagger UI: `http://localhost:9898/swagger-ui.html`
- OpenAPI JSON: `http://localhost:9898/v3/api-docs`

Most endpoints require a JWT bearer token. Obtain one by calling `GET /auth/login` with valid credentials and include it as `Authorization: Bearer <token>` when using the Swagger UI or other clients.
