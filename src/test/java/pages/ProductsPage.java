package pages;

import models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPage extends BasePage {
    // антипаттерн: god-object-page-object
    // созданы отдельные классы страниц

    private final By addProductButton = By.id("add-product-btn");
    private final By productNameField = By.id("product-name");
    private final By productPriceField = By.id("product-price");
    private final By saveButton = By.id("save-product-btn");
    private final By productsLinkButton = By.linkText("Продукты");
    private final By productInputField   = By.cssSelector("[data-testid='product-input-field']");
    private final By productList         = By.cssSelector("[data-testid='autocomplete-option']");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public void openProductCatalog() {
        waitAndClick(productsLinkButton);
    }

    public void createProduct(Product product) {
        waitAndClick(addProductButton);
        driver.findElement(productNameField).sendKeys(product.getName());
        driver.findElement(productPriceField).sendKeys(String.valueOf(product.getPrice()));
        waitAndClick(saveButton);
        waitForInvisibility(saveButton);
    }

    public void selectFirstProduct(String name) {
        waitAndFill(productInputField, name);
        waitAndClick(productList);
        driver.findElements(productList).get(0).click();
    }

    public void deleteProduct(String name) {
        // здесь типа удаляется продукт
    }
}
