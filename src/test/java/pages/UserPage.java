package pages;

import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UserPage extends BasePage {

    private final By usersLink = By.linkText("Пользователи");
    private final By addUserButton = By.id("add-user-btn");
    private final By nameField = By.id("user-name");
    private final By emailField = By.id("user-email");
    private final By roleField = By.id("user-role");
    private final By saveButton = By.id("save-user-btn");
    private final String userRowSelector = "[data-testid='user-row-%s']";
    private final String deleteButtonSelector = " [data-testid='delete-user-btn']";

    public UserPage(WebDriver driver) {
        super(driver);
    }

    public void openUsersList() {
        waitAndClick(usersLink);
    }

    public void createUser(User user) {
        waitAndClick(addUserButton);
        driver.findElement(nameField).sendKeys(user.getName());
        driver.findElement(emailField).sendKeys(user.getEmail());
        driver.findElement(roleField).sendKeys(user.getRole().name());
        waitAndClick(saveButton);
        waitForInvisibility(saveButton);
    }

    public void deleteUser(String email) {
        waitAndClick(By.cssSelector(String.format(userRowSelector, email) + deleteButtonSelector));
    }
}
