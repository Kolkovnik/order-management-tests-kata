import config.TestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.LoginPage;
import pages.OrdersPage;

public abstract class BaseTest {
    // антипаттерн: general-fixture
    // используется только логин
    // антипаттерн: no-page-object
    // убраны driver.findElement из тестов и вынесены в методы отдельных страниц
    // антипаттерн: singleton-web-driver
    // каждый тест в @BeforEeach получает нвоый экземпляр
    protected WebDriver driver;
    protected OrdersPage ordersPage;

    @BeforeEach
    public void setUp() {
        // антипаттерн: lonely-test
        // каждый тест теперь самостоятельный
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(TestConfig.BASE_URL);

        this.ordersPage = new LoginPage(driver)
                .login(TestConfig.ADMIN_LOGIN, TestConfig.ADMIN_PASSWORD);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
