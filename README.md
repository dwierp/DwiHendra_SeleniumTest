# Automation Testing with Selenium, Maven, TestNG, and Java on IntelliJ
## Project Overview
This project involves creating an automated test suite for a CURA Healthcare Service web application using Selenium WebDriver, Maven, TestNG, and Java. The goal is to automate the end-to-end workflow of booking an appointment, including login, filling out appointment details, and verifying the confirmation. The project will also integrate Allure Reporting for detailed test reporting and WebDriverManager for managing browser drivers.

## Scope of Work
### 1. Application Under Test:
- CURA Healthcare Service: A demo web application for booking medical appointments.
- URL: https://katalon-demo-cura.herokuapp.com/.
### 2. Tools:
- Selenium WebDriver: For browser automation.
- Maven: For dependency management and project build.
- TestNG: For test execution and reporting.
- Allure Framework: For generating detailed test reports.
- WebDriverManager: For managing browser drivers automatically.
- IntelliJ IDEA: As the Integrated Development Environment (IDE).
### 3. Deliverables:
- A Maven-based Java project with TestNG test cases.
- Allure reports for test execution.
- Documentation of test scenarios and results.

## Test Scenarios
### 1. Login Functionality
- Steps:
1. Navigate to the CURA Healthcare Service homepage.
2. Click the "Make Appointment" button.
3. Verify the login page is displayed.
4. Enter valid credentials (Username: John Doe, Password: ThisIsNotAPassword).
5. Verify successful login by checking the "Make Appointment" page.

### 2. Appointment Booking
- Steps:
1. Select a healthcare facility (e.g., "Hongkong CURA Healthcare Center").
2. Check the "Hospital Readmission" checkbox.
3. Select a healthcare program (e.g., "Medicaid").
4. Enter a visit date (e.g., "28/02/2025").
5. Add a comment (e.g., "Cabut Gigi").
6. Click the "Book Appointment" button.
7. Verify the appointment confirmation page and details.

### 3. Negative Test Cases (Optional for Future Scope)
- Invalid login credentials.
- Missing required fields during appointment booking.
- Invalid date format.

## Project Setup
### 1. Dependencies (Maven pom.xml)
<dependencies>
    <!-- Selenium WebDriver -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.28.1</version>
    </dependency>

    <!-- TestNG -->
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.10.2</version>
    </dependency>

    <!-- WebDriverManager -->
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.9.2</version>
    </dependency>

    <!-- Allure TestNG Integration -->
    <dependency>
        <groupId>io.qameta.allure</groupId>
        <artifactId>allure-testng</artifactId>
        <version>2.13.9</version>
    </dependency>
</dependencies>

### 2. Project Structure
src<br>
├── main<br>
│   └── java<br>
│       └── com<br>
│           └── example<br>
│               └── tests<br>
│                   └── CURAHealthcareTest.java<br>
└── test<br>
    └── resources<br>
        └── allure.properties<br>

### 3. Test Class (CURAHealthcareTest.java)
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.time.Duration;

@Listeners({AllureTestNg.class})
public class CURAHealthcareTest {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    @Description("Test to verify login and appointment booking functionality")
    public void testAppointmentBooking() {
        Reporter.log("Starting test execution", true);
        driver.get("https://katalon-demo-cura.herokuapp.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Login
        Reporter.log("Clicking Make Appointment button", true);
        driver.findElement(By.id("btn-make-appointment")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[.='Login']")));
        Assert.assertEquals(driver.findElement(By.xpath("//h2[.='Login']")).getText(), "Login");

        Reporter.log("Entering login credentials", true);
        driver.findElement(By.id("txt-username")).sendKeys("John Doe");
        driver.findElement(By.id("txt-password")).sendKeys("ThisIsNotAPassword");
        driver.findElement(By.id("btn-login")).click();

        // Verify login success
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[.='Make Appointment']")));
        Assert.assertEquals(driver.findElement(By.xpath("//h2[.='Make Appointment']")).getText(), "Make Appointment");

        // Book Appointment
        Reporter.log("Filling appointment details", true);
        driver.findElement(By.id("combo_facility")).sendKeys("Hongkong CURA Healthcare Center");
        driver.findElement(By.id("chk_hospotal_readmission")).click();
        driver.findElement(By.id("radio_program_medicaid")).click();
        WebElement dateInput = driver.findElement(By.id("txt_visit_date"));
        dateInput.clear();
        dateInput.sendKeys("28/02/2025");
        driver.findElement(By.id("txt_comment")).sendKeys("Cabut Gigi");
        driver.findElement(By.id("btn-book-appointment")).click();

        // Verify appointment confirmation
        Reporter.log("Verifying appointment confirmation", true);
        verifyElement("//h2[.='Appointment Confirmation']", "Appointment Confirmation");
        verifyElement("//p[@id='facility']", "Hongkong CURA Healthcare Center");
        verifyElement("//p[@id='hospital_readmission']", "Yes");
        verifyElement("//p[@id='program']", "Medicaid");
        verifyElement("//p[@id='visit_date']", "28/02/2025");
        verifyElement("//p[@id='comment']", "Cabut Gigi");
    }

    @Step("Verify element with xpath: {xpath} and expected text: {expectedText}")
    private void verifyElement(String xpath, String expectedText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        String actualText = driver.findElement(By.xpath(xpath)).getText();
        Reporter.log("Verifying element: " + xpath + " Expected: " + expectedText + " Actual: " + actualText, true);
        Assert.assertEquals(actualText, expectedText);
    }

    @AfterMethod
    public void tearDown() {
        Reporter.log("Closing browser", true);
        if (driver != null) {
            driver.quit();
        }
    }
}

### 4. Allure Configuration (allure.properties)
allure.results.directory=target/allure-results

## Execution and Reporting
### 1. Run Tests:
- Execute tests using TestNG in IntelliJ IDEA.
- Use the command mvn test to run tests via Maven.
### 2. Generate Allure Report:
- Run mvn allure:serve to generate and view the Allure report.

## Expected Results
- All test steps should pass successfully.
- Detailed Allure reports should be generated, including steps, screenshots (if configured), and assertions.

## Timeline
- Day 1-2: Set up the project and write test cases.
- Day 3-4: Execute tests and generate reports.
- Day 5: Finalize documentation and deliver the project.
