# onlineShopApp automated QA

External automated test project for [`stelian-mihalceanu/onlineShopApp`](https://github.com/stelian-mihalceanu/onlineShopApp).

## Test layers

- Maven/JUnit 5 API smoke tests against the running Spring Boot app.
- Spring Boot integration tests from the production application's own test suite are executed in CI.
- Selenium end-to-end browser flow for `register → login → product → cart → checkout`.
- PostgreSQL service in GitHub Actions so Flyway and `spring.jpa.hibernate.ddl-auto=validate` are exercised against a real database.

## Local API/E2E execution

Start the application on `http://127.0.0.1:8080`, then run:

```bash
mvn -B test -DbaseUrl=http://127.0.0.1:8080
```

The Selenium suite expects Chrome/Chromedriver to be available and defaults to headless mode.

## CI

`.github/workflows/qa.yml` starts PostgreSQL, runs the production Maven tests, builds and starts the application, waits for `/actuator/health`, then executes external API and Selenium tests.
