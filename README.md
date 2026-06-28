# e2e-ai-automation

API automation framework for the [ReqRes](https://reqres.in) Users API using **Java 21**, **Cucumber 7 BDD**, and **Rest Assured**.

## Stack
| Layer | Technology |
|---|---|
| Language | Java 21 |
| BDD | Cucumber 7 + JUnit Platform Suite |
| HTTP Client | Rest Assured 5 |
| DI | PicoContainer |
| Reporting | Allure 2 + Cucumber HTML |
| Build | Maven 3 |

## Project Structure
```
src/test/
├── java/com/reqres/
│   ├── client/        # ApiClient (HTTP methods)
│   ├── config/        # ApiConfig (RequestSpec), ConfigManager
│   ├── context/       # TestContext (PicoContainer DI state)
│   ├── hooks/         # Cucumber @Before/@After
│   ├── model/         # POJOs / Records (request & response models)
│   ├── runner/        # RunCucumberTest (JUnit Suite)
│   └── steps/         # Step definitions
└── resources/
    ├── features/      # .feature files
    ├── config.properties
    ├── allure.properties
    └── junit-platform.properties
```

## Running Tests

```bash
# Run all tests
mvn test

# Run only @smoke tagged tests
mvn test -Dcucumber.filter.tags="@smoke"
```

## Reports

```bash
# Open Allure report (after mvn test)
allure serve target/allure-results

# Cucumber HTML report
open target/cucumber-reports/cucumber.html
```
