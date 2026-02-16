# API Automation Framework

This project is a robust and scalable API automation framework built using **Java**, **RestAssured**, and **TestNG**. It is designed to test RESTful APIs with support for Data-Driven Testing (DDT), parallel execution, and detailed reporting.

## 🚀 Tech Stack

*   **Language:** Java 21+
*   **API Client:** RestAssured
*   **Test Runner:** TestNG
*   **Build Tool:** Maven
*   **Reporting:** Allure Report
*   **Data Handling:** Jackson (JSON serialization/deserialization)
*   **Data Generation:** JavaFaker
*   **Logging:** Log4j2 / SLF4J

## 📦 Dependencies

The project relies on the following key libraries (versions as defined in `pom.xml`):

| Dependency | Version | Purpose |
| :--- | :--- | :--- |
| `io.rest-assured:rest-assured` | `5.5.6` | Fluent API for testing REST services |
| `org.testng:testng` | `7.9.0` | Test framework for executing tests and assertions |
| `com.fasterxml.jackson.core:jackson-databind` | `2.17.0` | JSON parsing and object mapping |
| `io.qameta.allure:allure-testng` | `2.25.0` | Generates comprehensive test reports |
| `com.github.javafaker:javafaker` | `1.0.2` | Generates fake data for negative testing |
| `org.apache.logging.log4j:log4j-core` | `2.23.1` | Logging framework |
| `com.google.code.gson:gson` | `2.10.1` | JSON serialization/deserialization (alternative to Jackson) |

## 📂 Project Structure

The framework follows a modular and maintainable structure:

```
src/test/java/org/example
├── apis_admin          # API classes for Admin endpoints (Login, ItemManager, OrderManager)
├── apis_customer       # API classes for Customer endpoints (Login, ItemViewer, OrderCustomerManager)
├── pojos_bodies        # POJO classes for Request and Response bodies
│   ├── request         # Request payloads
│   └── response        # Response payloads
├── providers_data      # Data Providers for TestNG (e.g., reading from JSON)
├── specs_variables     # Base API specifications and constant error messages
├── tests_scripts       # Test classes (ValidTests, InvalidTests, FlowTestFactory)
└── utils               # Utility classes (ConfigManager, JsonReader, Generators)
```

## ⚙️ Configuration

### 1. Environment Properties
Configure your environment variables in `src/test/resources/config.properties`:

```properties
base.url=http://localhost:3000
admin.username=admin
admin.password=adminpassword
customer.username=user2
customer.password=userpassword
```

### 2. Test Data
User data for data-driven testing is stored in `src/test/resources/users.json`.

## 🏃‍♂️ How to Run Tests

### Run via Maven
To execute the entire test suite defined in `testing.xml`:

```bash
mvn clean test
```

### Run via TestNG XML
You can also run the tests directly from your IDE by right-clicking on `testing.xml` and selecting **Run**.

### Parallel Execution
Parallel execution is configured in `testing.xml`.
*   **Valid Tests:** Run in parallel by instance (one thread per user flow).
*   **Invalid Tests:** Run in parallel by method.

## 📊 Reporting

This framework uses **Allure** for reporting. To generate and view the report after a test run:

1.  Run the tests:
    ```bash
    mvn clean test
    ```
2.  Generate and serve the report:
    ```bash
    mvn allure:serve
    ```
    This command will start a local web server and open the report in your default browser.

## 🔐 Token Management Strategy

The framework implements a secure and efficient token management strategy:

1.  **Authentication:** In the `@BeforeClass` setup method of test classes (e.g., `ValidTests`, `InvalidTests`), the framework performs login requests for both Admin and Customer roles.
2.  **Extraction:** The JWT tokens are extracted from the JSON response using RestAssured's `jsonPath()`.
3.  **Storage:** Tokens are stored in private instance variables (`adminToken`, `customerToken`) within the test class.
4.  **Reuse:** These tokens are passed as arguments to subsequent API method calls (e.g., `itemManager.addItem(adminToken, ...)`), ensuring that each test method uses a valid, authenticated session without needing to re-login.

## 🧪 Key Features

*   **Data-Driven Testing:** Uses `FlowTestFactory` to run end-to-end flows for multiple users defined in `users.json`.
*   **Negative Testing:** `InvalidTests` covers edge cases, invalid inputs, and authorization failures using `JavaFaker` for random data.
*   **POJO Mapping:** strict type checking and cleaner code using Jackson for JSON serialization.
*   **Singleton/Static Utilities:** Efficient management of configuration and shared resources.
*   **CI/CD Ready:** Can be easily integrated into Jenkins or GitHub Actions via Maven commands.

---

## 🔍 Code Walkthrough & Sample Test Cases

### 1. Data-Driven End-to-End Testing

This snippet shows how the `FlowTestFactory` uses TestNG's `@Factory` to create a new `ValidTests` instance for each customer in `users.json`. This allows the entire end-to-end flow to run for multiple users.

```java
// In FlowTestFactory.java
@Factory(dataProvider = "userDataProvider")
public Object[] createInstances(UserData admin, UserData customer) {
    // Creates a new instance of the ValidTests class, passing the admin and customer data.
    // TestNG will run all @Test methods in ValidTests for this specific user combination.
    return new Object[]{new ValidTests(admin, customer)};
}

// In ValidTests.java
public class ValidTests {
    private UserData adminData;
    private UserData customerData;

    // The constructor receives the data from the factory
    public ValidTests(UserData adminData, UserData customerData) {
        this.adminData = adminData;
        this.customerData = customerData;
    }

    @BeforeClass
    public void Login() {
        // The login method now uses the specific user data for this test instance
        System.out.println("Customer: " + customerData.getUsername());
        // ... login logic ...
    }
}
```

### 2. API Abstraction Layer

This snippet from `ItemManager.java` demonstrates how API calls are encapsulated. The test scripts don't need to know about URLs, headers, or HTTP methods. They just call a clean Java method.

```java
// In ItemManager.java
public class ItemManager {
    private static final String ITEM_ENDPOINT="/items";

    public Response addItem(String token, AddItemPojo body) {
        return given()
                // Reuses the base specification (Base URL, Content-Type)
                .spec(BaseApi.getRequestSpecification()) 
                .header("Authorization", "Bearer " + token)
                .body(body)
            .when()
                .post(ITEM_ENDPOINT);
    }
}

// In a test script
@Test
public void addItemsTest() {
    // ... setup ...
    ItemManager itemManager = new ItemManager();
    // The test calls a simple Java method, making it clean and readable
    itemManager.addItem(adminToken, addItems) 
        .then()
        .statusCode(201);
}
```

### 3. Negative Testing with Data Generation

This snippet from `InvalidTests.java` shows how to test for error conditions. It uses `CredentialsGenerator` (which uses JavaFaker) to create invalid data and asserts that the API returns the expected error message.

```java
// In InvalidTests.java
@Test
public void specialChUserNameTest() {
    CustomerLoginPojo credentials = new CustomerLoginPojo();
    // Generates a username with special characters that are not allowed
    credentials.setUsername(CredentialsGenerator.generateSpecialChUserName()); 
    credentials.setPassword(CredentialsGenerator.generateRandomPassword());
    
    CustomerLogin customerLogin = new CustomerLogin();
    customerLogin.login(credentials)
        .then()
        .statusCode(400)
        // Asserts that the response body contains the correct error message
        .body("error", equalTo(ErrorMessages.USERNAME_ALPHANUMERIC)); 
}
```

## ⚠️ Limitations & Future Enhancements

### Limitations
*   **Synchronization:** Currently uses `Thread.sleep()` to handle server processing delays. This is not ideal for performance and can make tests flaky if the server is slower than the sleep time.
*   **Hardcoded IDs:** Some negative tests rely on specific IDs (e.g., for "Not Found" scenarios) which might need manual updates if the database is reset.

### Future Enhancements
*   **Awaitility Integration:** Replace `Thread.sleep()` with Awaitility to poll for state changes dynamically, making tests faster and more reliable.
*   **Database Integration:** Connect directly to the database to verify data persistence and clean up test data after execution.
*   **Dockerization:** Containerize the test environment to run consistently across different machines.
*   **CI/CD Pipeline:** Set up a Jenkins or GitHub Actions pipeline to run tests automatically on every commit.

## 📝 License

This project is open-source and available under the MIT License.
