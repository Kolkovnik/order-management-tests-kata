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

    public OrdersPage login(String user, String password) {
        // антипаттерн: irrelevant-information
        // реализация скрыта от теста
        waitAndFill(usernameField, user);
        waitAndFill(passwordField, password);
        waitAndClick(loginButton);
        waitForInvisibility(loginButton);
        return new OrdersPage(driver);
    }
}
