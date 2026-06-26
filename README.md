# ReqRes Users API — E2E Automation

API automation for the [ReqRes](https://reqres.in) Users API, built with **Cucumber BDD + Rest Assured + JUnit 5** following the team's API Automation Best Practices.

Test coverage maps 1:1 to [`test-cases-reqres-users-api.md`](../test-cases-reqres-users-api.md) — all ~50 cases across Authentication, Create User, List Users, and Cross-Cutting Concerns.

## Stack

| Concern              | Choice                                            |
|----------------------|---------------------------------------------------|
| Language / build     | Java 21, Maven                                    |
| BDD                  | Cucumber 7 (`cucumber-junit-platform-engine`)     |
| HTTP / assertions    | Rest Assured 5, Hamcrest                          |
| State sharing        | PicoContainer DI (`TestContext`, no `static`)     |
| Reporting            | Allure + Cucumber HTML/JSON                        |

## Project layout

```
src/test/
├── java/com/reqres/
│   ├── runner/RunCucumberTest.java     # JUnit 5 Suite → Cucumber engine
│   ├── config/                         # ConfigManager, ApiConfig (RequestSpec)
│   ├── client/ApiClient.java           # Rest Assured wrapper, builds each request
│   ├── context/TestContext.java        # per-scenario shared state (DI)
│   ├── model/                          # Java records: request/response POJOs
│   ├── hooks/Hooks.java                # @Before/@After, Allure attachments
│   └── steps/                          # step definitions (thin, declarative)
└── resources/
    ├── features/                       # *.feature, one per test-case section
    ├── config.properties               # base.uri, api.key
    ├── junit-platform.properties       # glue + plugins
    └── allure.properties
```

## Running

```bash
# All scenarios
mvn test

# By tag
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@create and not @negative"

# Override config at runtime
mvn test -Dapi.key=YOUR_KEY -Dbase.uri=https://reqres.in
```

## Reports

- **Cucumber HTML:** `target/cucumber-reports/cucumber.html` (open directly)
- **Allure:** `mvn allure:serve` (or `allure serve target/allure-results`) — full request/response payloads are attached per scenario.

## ⚠️ Known live-API drift (as of 2026-06-26)

The test cases were written against an earlier state of reqres.in. Two things have since changed on the live service:

1. **The public key `reqres-free-v1` is no longer accepted.** Every authenticated request returns `401 missing_api_key`, so all positive scenarios fail until a valid key is supplied. Get a free personal key at [app.reqres.in](https://app.reqres.in/api-keys) and run with `-Dapi.key=YOUR_KEY` (or edit `config.properties`).
2. **The auth error body changed shape** — it now returns
   `{"error":"missing_api_key","message":...,"hint":...,"next_steps":[...]}`
   instead of the doc's `{"error":"Missing API key.","how_to_get_one":...}`.
   Scenarios that assert the exact legacy error text (TC-AUTH-002, TC-CRU-010, TC-LST-013) will therefore fail against the current API — this is the suite correctly detecting contract drift. Update either the test-case doc or these assertions once the expected contract is confirmed.

The framework itself is verified end-to-end: requests fire with headers, responses are captured, assertions evaluate, and reports/attachments are produced.

## Contribution workflow

Never push to `main` directly. Branch (`feature/TC-xyz-...`) → commit → open an MR targeting `main` → green CI + 1 peer SDET approval → merge.
