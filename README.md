# SauceDemo UI Automation

Selenium + TestNG + Maven test suite for [saucedemo.com](https://www.saucedemo.com/), built with the Page Object Model and wired into GitHub Actions.

## What is tested

| Test class | Scenario | TestNG groups |
|---|---|---|
| `LoginTest` | Successful login with `standard_user` | `smoke` |
| `SortingTest` | Products sorted by name A-Z and Z-A | `sorting` |
| `CartTest` | Add a product, remove it, add it again | `cart` |
| `CheckoutTest.completeCheckout` | Full checkout with First name `ABC`, Last name `DEF`, Zip `123456` | `smoke`, `checkout` |
| `CheckoutTest.checkoutWithoutFirstNameShowsError` | First name left blank → `Error: First Name is required` | `checkout`, `negative` |
| `SocialLinksTest` | Twitter (now **X**) and Facebook footer icons open the correct page | `social` |

> SauceDemo has renamed its Twitter icon to **X**: it now points to `https://x.com/saucelabs`. The test checks that link and accepts the page opening on either `x.com` or `twitter.com`.

## Prerequisites

- **Java 17+** (JDK, not just a JRE). Check with `java -version`.
- **Google Chrome**, any recent version.
- **Maven is optional.** The project includes the Maven Wrapper (`mvnw` / `mvnw.cmd`), which downloads the right Maven version on first use.

You don't need to download ChromeDriver. Selenium Manager (built into Selenium 4) fetches the version that matches your Chrome.

## Running the tests

```bash
# Everything (Chrome window visible)
./mvnw clean test            # macOS / Linux / Git Bash
mvnw.cmd clean test          # Windows cmd / PowerShell

# Headless
./mvnw clean test -Dheadless=true

# Only some groups
./mvnw clean test -Dgroups=smoke
./mvnw clean test -Dgroups=checkout,negative

# A single class
./mvnw clean test -Dtest=CartTest
```

If Maven is installed you can use `mvn` instead of `./mvnw`.

Settings live in [src/test/resources/config.properties](src/test/resources/config.properties) (URL, credentials, wait timeout). Any value can be overridden on the command line, e.g. `-DtimeoutSeconds=20`.

## Reports and logs

After a run, look in `target/`:

| File | What it is |
|---|---|
| `target/surefire-reports/emailable-report.html` | **Start here.** One-page HTML summary: pass/fail per test, duration, failure messages |
| `target/surefire-reports/index.html` | Full TestNG report: browse by suite, group, class, with stack traces |
| `target/surefire-reports/testng-results.xml` | Machine-readable XML results |
| `target/surefire-reports/junitreports/*.xml` | JUnit-style XML (understood by Jenkins, GitLab, etc.) |
| `target/logs/test-run.log` | Console log: start / pass / fail of each test |
| `target/screenshots/*.png` | Screenshot of the browser for every failed test (only created when something fails) |

An example of each report from a green run is in [docs/sample-report/](docs/sample-report/).

## CI/CD (GitHub Actions)

The pipeline is in [.github/workflows/tests.yml](.github/workflows/tests.yml). It runs:

- on every push to `main` / `master` and on every pull request
- on demand: **Actions → SauceDemo UI Tests → Run workflow**, optionally entering groups such as `smoke`

Steps: check out the code → install Java 17 → `./mvnw clean test` (headless; Chrome is preinstalled on the runner) → upload the reports.

Each run has a short result line on its summary page and a downloadable **`test-report`** artifact. The artifact contains the HTML/XML reports, the console log and any failure screenshots.

You don't need to configure anything. Push the repo to GitHub and the workflow appears in the Actions tab. Tests run headless automatically on any CI server because the `CI` environment variable is set there.

## Project structure

```
├── .github/workflows/tests.yml     CI pipeline
├── pom.xml                         dependencies: Selenium, TestNG
├── testng.xml                      suite: test classes + listener
├── docs/sample-report/             example reports
└── src/test/
    ├── resources/config.properties
    └── java/com/saucedemo/
        ├── pages/                  Page Objects
        │   ├── BasePage            driver, shared helpers (click, type, ...), abstract isLoaded()
        │   ├── AppPage             header + footer shared by every page after login
        │   ├── LoginPage, ProductsPage, CartPage,
        │   │   CheckoutInfoPage, CheckoutOverviewPage, CheckoutCompletePage
        │   ├── SortOrder           sort options (enum with its own comparator per option)
        │   └── SocialNetwork       footer social links and their expected URLs
        ├── tests/                  BaseTest (browser setup/teardown, login helper) + test classes
        ├── utils/                  DriverFactory, Config, Waits, Log, ScreenshotUtil
        └── listeners/TestListener  logs each test, screenshots on failure
```

### OOP and polymorphism

- **Inheritance:** `BasePage` → `AppPage` → concrete pages. Shared actions such as `click`, `type`, `openCart()` and the social-link handling are written once.
- **Polymorphism 1, `isLoaded()`:** an abstract method in `BasePage`, overridden by each page with its own check (URL plus page heading). `BasePage.waitFor(page)` calls it on any page type, and every navigation method uses it, e.g. `return waitFor(new CartPage(driver));`.
- **Polymorphism 2, `SortOrder`:** each enum constant overrides `comparator()`. `SortingTest` runs the same code for A-Z and Z-A, and `order.sort(names)` returns the expected order without any `if/else`.
- **Fluent page objects:** actions return the next page, so tests read like the user journey:
  `loginAsStandardUser().addToCart(BACKPACK).openCart().checkout()`

## Extending

- **New page:** extend `AppPage` (or `BasePage` for pages before login), add `By` locators, implement `isLoaded()`.
- **New test:** extend `BaseTest`, tag it with a group, and add the class to `testng.xml`.
- **Locators:** use SauceDemo's `data-test` attributes where possible; they are the most stable.
- **Login:** `LoginPage.loginAs()` expects success and waits for the products page. For invalid-login tests, add a method that clicks login and stays on `LoginPage` (then use `getError()`).
