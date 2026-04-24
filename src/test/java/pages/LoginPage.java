package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-btn");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void openMainPage() {
        driver.get("https://orders.internal.example.com");
    }

    public void login(String user, String pass) {
        waitAndFill(usernameField, user);
        driver.findElement(passwordField).sendKeys(pass);
        waitAndClick(loginButton);
        waitForInvisibility(loginButton);
    }
}
