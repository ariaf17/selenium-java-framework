# selenium-java-framework
# Selenium Java Framework (Enterprise-Style)

A portfolio-grade UI automation framework showcasing:
- **Selenium WebDriver (Java 17)** with clean **OOP + Page Object Model**
- **Two-track strategy**:
  - **BDD acceptance layer**: Cucumber + JUnit 5 (feature-driven, tag-based)
  - **Classic enterprise suite**: TestNG (groups, suites, data-driven patterns)
- Maven profiles for clean execution and CI friendliness

## Tech Stack
- Java 17
- Maven
- Selenium 4
- Cucumber (BDD) + JUnit 5
- TestNG
- WebDriverManager
- Log4j2
- Allure reporting (wired in later commits)

## Project Structure (high level)
- `core/` shared framework utilities (driver, config, waits, screenshots)
- `pages/` Page Objects (POM)
- `bdd/` Cucumber runners/steps/hooks
- `testng/` TestNG base/listeners/tests
- `src/test/resources/features/` Gherkin feature files
- `src/test/resources/config/` environment configs

## Running tests

### BDD (Cucumber)
```bash
mvn test -Pbdd

## CI (GitHub Actions)
- PR Smoke: runs **BDD smoke + TestNG smoke**, uploads Cucumber report + screenshots + Allure results
- Nightly Regression: runs **full TestNG suite**, generates and uploads **Allure HTML report**

## CI-equivalent local commands
```bash
mvn test -Pbdd -Denv=ci -Dheadless=true
mvn test -Ptestng -Denv=ci -Dheadless=true -Dtestng.groups=smoke
mvn test -Ptestng -Denv=ci -Dheadless=true
mvn allure:report

