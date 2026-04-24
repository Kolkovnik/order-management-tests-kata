package pages;

import models.OrderStatus;
import models.PaymentMethod;
import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class OrdersPage extends BasePage {

    // Навигация
    private final By createOrderButton   = By.cssSelector("[data-testid='create-order-btn']");
    private final By submitOrderButton   = By.cssSelector("[data-testid='submit-order-btn']");
    private final By ordersLinkButton    = By.linkText("Заказы");
    private final By exportButton        = By.id("export-btn");

    // Форма создания заказа
    private final By productInputField   = By.cssSelector("[data-testid='product-input-field']");
    private final By productList         = By.cssSelector("[data-testid='autocomplete-option']");
    private final By quantityInputField  = By.cssSelector("[data-testid='quantity-input-field']");
    private final By customerNameField   = By.id("customer-name");
    private final By customerPhoneField  = By.id("customer-phone");
    private final By customerAddressField = By.id("customer-address");
    private final By paymentMethodField  = By.id("payment-method");
    private final By cardNumberField     = By.id("card-number");

    // Детали заказа
    private final By orderIdLabel        = By.cssSelector(".created-order-id");
    private final By detailName          = By.cssSelector("[data-testid='detail-customer-name']");
    private final By detailPhone         = By.cssSelector("[data-testid='detail-customer-phone']");
    private final By detailAddress       = By.cssSelector("[data-testid='detail-customer-address']");
    private final By detailQuantity      = By.cssSelector("[data-testid='detail-order-quantity']");
    private final By detailTotal         = By.cssSelector("[data-testid='detail-order-total']");
    private final By createdAtLabel      = By.cssSelector("[data-testid='detail-created-at']");
    private final By errorBlock          = By.cssSelector("[data-testid='error-notification']");
    //// Если в теории появляется уведомление об успешном скачивании файла
    private final By successNotification = By.cssSelector("[data-testid='notification-success']");

    // Список заказов
    private final By searchInput         = By.id("search-input");
    private final By searchButton        = By.id("search-btn");
    private final By tableRows           = By.cssSelector("[data-testid='order-row']");
    private final By customerNameCell    = By.cssSelector("[data-testid='customer-name-cell']");
    private final String orderStatusSelector   = " [data-testid='order-status']";
    private final String orderRowSelector      = "[data-testid='order-row-%s']";
    private final String approveButtonSelector = " [data-testid='approve-button']";
    private final String cancelButtonSelector  = " [data-testid='cancel-button']";

    public OrdersPage(WebDriver driver) {
        super(driver);
    }

    public void clickCreateOrder() {
        waitAndClick(createOrderButton);
    }

    public void selectFirstProduct(String name) {
        waitAndFill(productInputField, name);
        waitAndClick(productList);
        driver.findElements(productList).get(0).click();
    }

    public void setQuantity(int qty) {
        waitAndFill(quantityInputField, String.valueOf(qty));
    }

    public void fillCustomer(User user) {
        waitAndFill(customerNameField, user.getName());
        driver.findElement(customerPhoneField).sendKeys(user.getPhone());
        driver.findElement(customerAddressField).sendKeys(user.getAddress());
    }

    public void fillPayment(User user) {
        waitAndFill(paymentMethodField, user.getPaymentMethod().name());
        if (user.getPaymentMethod() == PaymentMethod.CARD) {
            driver.findElement(cardNumberField).sendKeys(user.getCardNumber());
        }
    }

    public void submitOrder() {
        waitAndClick(submitOrderButton);
    }

    public String getOrderId() {
        return waitAndGetText(orderIdLabel);
    }

    public OrderStatus getOrderStatus(String orderId) {
        String statusText = waitAndGetText(By.cssSelector(String.format(orderRowSelector, orderId) + orderStatusSelector));
        return OrderStatus.valueOf(statusText.toUpperCase());
    }

    /**
     * Нажать на кнопку подтверждения/отмены заказа
     * @param actionSelector CSS-селектор конкретной кнопки действия
     */
    private void clickOrderAction(String orderId, String actionSelector) {
        waitAndClick(By.cssSelector(String.format(orderRowSelector, orderId) + actionSelector));
    }

    /**
     * Нажать на кнопку подтверждения заказа
     */
    public void clickApprove(String orderId) {
        clickOrderAction(orderId, approveButtonSelector);
    }

    /**
     * Нажать на кнопку отмены заказа
     */
    public void clickCancel(String orderId) {
        clickOrderAction(orderId, cancelButtonSelector);
    }

    /**
     * Нажать на кнопку со ссылкой на 'Заказы'
     */
    public void clickOrdersLink() {
        waitAndClick(ordersLinkButton);
    }

    /**
     * Получить имя заказчика
     */
    public String getDetailCustomerName() {
        return waitAndGetText(detailName);
    }

    /**
     * Получить номер телефона заказчика
     */
    public String getDetailCustomerPhone() {
        return driver.findElement(detailPhone).getText();
    }

    /**
     * Получить адрес доставки
     */
    public String getDetailCustomerAddress() {
        return driver.findElement(detailAddress).getText();
    }

    /**
     * Получить количество товаров
     */
    public String getDetailQuantity() {
        return driver.findElement(detailQuantity).getText();
    }

    /**
     * Получить итоговую сумму заказа
     */
    public String getDetailTotal() {
        return driver.findElement(detailTotal).getText();
    }

    /**
     * Отображение даты создания заказа
     */
    public boolean isCreatedAtDisplayed() {
        return isDisplayed(createdAtLabel);
    }

    /**
     * Проверка отображения блока с ошибками
     */
    public boolean isErrorBlockDisplayed() {
        return !driver.findElements(errorBlock).isEmpty();
    }

    /**
     * Поиск заказа по имени заказчика
     */
    public void searchOrderByCustomer(String name) {
        waitAndFill(searchInput, name);
        waitAndClick(searchButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(tableRows));
    }

    /**
     * Получить список имён заказчиков
     */
    public List<String> getVisibleCustomerNames() {
        return driver.findElements(tableRows).stream()
                .map(row -> row.findElement(customerNameCell).getText())
                .toList();
    }

    /**
     * Нажать на кнопку выгрузки заказов
     */
    public void clickExportButton() {
        waitAndClick(exportButton);
    }

    /**
     * Проверка успешной выгрузки заказа
     */
    public boolean isExportStarted() {
        return driver.findElement(exportButton).isEnabled()
                // Если появляется уведомление об успешном скачивании
                && isDisplayed(successNotification);
    }
}
