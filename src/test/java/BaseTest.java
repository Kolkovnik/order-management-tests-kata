import config.AppConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.LoginPage;

public abstract class BaseTest {
    protected static final AppConfig config = new AppConfig("application-test.properties.example");
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openMainPage();
        loginPage.login(
                config.getString("TEST_ADMIN_LOGIN"),
                config.getString("TEST_ADMIN_PASSWORD")
        );
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
