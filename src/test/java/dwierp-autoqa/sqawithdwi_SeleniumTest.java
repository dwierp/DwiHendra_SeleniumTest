import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
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
import java.io.File;
import java.time.Duration;

@Listeners({AllureTestNg.class})
public class sqawithdwi_SeleniumTest {
   //DwiHendra Automatic Testing - Selenium Maven TestNg Java IntelliJ

    WebDriver driver;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Create Allure results directory if it doesn't exist
        File resultsDir = new File("target/allure-results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }
    }

    @Test
    @Description("Test to verify login functionality")
    public void loginTest() {
        Reporter.log("Starting test execution", true);
        driver.get("https://katalon-demo-cura.herokuapp.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        Reporter.log("Clicking Make Appointment button", true);
        driver.findElement(By.xpath("//a[@id='btn-make-appointment']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[.='Login']")));
        String login = driver.findElement(By.xpath("//h2[.='Login']")).getText();
        Reporter.log("Verifying login page", true);
        Assert.assertEquals(login, "Login");

        Reporter.log("Entering login credentials", true);
        driver.findElement(By.xpath("//input[@id='txt-username']")).sendKeys("John Doe");
        driver.findElement(By.xpath("//input[@id='txt-password']")).sendKeys("ThisIsNotAPassword");
        driver.findElement(By.xpath("//button[@id='btn-login']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[.='Make Appointment']")));
        String textMakeAppointmentActual = driver.findElement(By.xpath("//h2[.='Make Appointment']")).getText();
        Reporter.log("Verifying successful login", true);
        Assert.assertEquals(textMakeAppointmentActual, "Make Appointment");

        Reporter.log("Filling appointment details", true);
        driver.findElement(By.xpath("//select[@id='combo_facility']"))
                .findElement(By.xpath("//option[@value='Hongkong CURA Healthcare Center']")).click();
        driver.findElement(By.id("chk_hospotal_readmission")).click();
        driver.findElement(By.id("radio_program_medicaid")).click();

        WebElement dateInput = driver.findElement(By.id("txt_visit_date"));
        dateInput.clear();
        dateInput.sendKeys("28/02/2025");

        driver.findElement(By.xpath("//textarea[@id='txt_comment']")).sendKeys("Cabut Gigi");
        driver.findElement(By.xpath("//button[@id='btn-book-appointment']")).click();

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
        Allure.step("Verified element: " + xpath + " Expected: " + expectedText + " Actual: " + actualText);
    }

    @AfterMethod
    public void tearDown() {
        Reporter.log("Closing browser", true);
        if (driver != null) {
            driver.quit();
        }
    }
}
