import factory.ProductFactory;
import factory.UserFactory;
import models.OrderStatus;
import models.Product;
import models.User;
import org.assertj.core.api.SoftAssertions;
import pages.OrdersPage;
import org.junit.jupiter.api.*;
import pages.ProductsPage;
import pages.UserPage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderManagementTest extends BaseTest {
    // антипаттерн: test-interdependency
    // не используется @Order, каждый тест создает свои данные в setupCustomerAndProduct()

    private OrdersPage ordersPage;
    private ProductsPage productsPage;
    private UserPage userPage;
    private User customer;
    private Product product;
    private String orderId;

    @BeforeEach
    void initPages() {
        userPage = new UserPage(driver);
        productsPage = new ProductsPage(driver);
        ordersPage = new OrdersPage(driver);
    }

    @AfterEach
    void cleanUp() {
        // антипаттерн: no-data-isolation
        // очистка данных после теста
        // антипаттерн: interacting-tests
        // сброс состояния, чтобы тесты не влияли друг на друга
        if (orderId != null) {
            ordersPage.deleteOrder(orderId);
        }

        if (customer != null) {
            userPage.openUsersList();
            userPage.deleteUser(customer.getEmail());
        }

        if (product != null) {
            productsPage.openProductCatalog();
            productsPage.deleteProduct(product.getName());
        }
    }

    private void setupCustomerAndProduct() {
        customer = UserFactory.defaultCustomer();
        userPage.openUsersList();
        userPage.createUser(customer);

        product = ProductFactory.defaultProduct();
        productsPage.openProductCatalog();
        productsPage.createProduct(product);

        ordersPage.clickOrdersLink();
    }

    /**
     * Создание базового заказа
     */
    private String createTestOrder(int quantity) {
        if (customer == null || product == null) {
            setupCustomerAndProduct();
        }
        ordersPage.clickCreateOrder();
        ordersPage.fillCustomer(customer);
        ordersPage.fillPayment(customer);
        productsPage.selectFirstProduct(product.getName());
        ordersPage.setQuantity(quantity);
        ordersPage.submitOrder();
        this.orderId = ordersPage.getOrderId();
        return this.orderId;
    }

    @Test
    void createOrderAndVerifyAllFields() {
        // антипаттерн: hard-coded-data
        // данные берутся из фабрик и конфига, а постоянно прописывается вручную
        // антипаттерн: irrelevant-information
        // в фабрике только значимые поля, остальное - дефолты
        // антипаттерн: eager-test
        // один тест проверяет одно поведение (создание заказа), а не всё сразу
        int quantity = 3;
        orderId = createTestOrder(quantity);

        // антипаттерн: assertion-roulette
        // softAssertions чтобы увидеть все ошибки за один прогон
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(ordersPage.getDetailCustomerName()).isEqualTo(customer.getName());
        soft.assertThat(ordersPage.getDetailCustomerPhone()).isEqualTo(customer.getPhone());
        soft.assertThat(ordersPage.getDetailCustomerAddress()).isEqualTo(customer.getAddress());
        soft.assertThat(ordersPage.getDetailQuantity()).isEqualTo(quantity);
        soft.assertThat(ordersPage.getDetailTotal()).isEqualTo(product.getFormattedPrice(quantity));
        soft.assertThat(ordersPage.getOrderStatus(orderId)).isEqualTo(OrderStatus.PENDING);
        soft.assertThat(ordersPage.isCreatedAtDisplayed()).isTrue();
        soft.assertThat(ordersPage.isErrorBlockDisplayed()).isFalse();
        soft.assertAll();
    }

    @Test
    void approveCreatedOrder() {
        // антипаттерн: test-interdependency
        // тест сам создает заказ и не зависит от других тестов
        orderId = createTestOrder(1);
        ordersPage.clickApprove(orderId);

        assertThat(ordersPage.getOrderStatus(orderId)).isEqualTo(OrderStatus.APPROVED);
    }

    @Test
    void cancelCreatedOrder() {
        orderId = createTestOrder(1);
        ordersPage.clickCancel(orderId);

        assertThat(ordersPage.getOrderStatus(orderId)).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void searchOrdersByCustomer() {
        createTestOrder(1);
        ordersPage.searchOrderByCustomer(customer.getName());
        // антипаттерн: conditional-test-logic
        // убраны циклы for и условия if
        List<String> customerNames = ordersPage.getVisibleCustomerNames();

        assertThat(customerNames).isNotEmpty()
                .allSatisfy(name -> assertThat(name).contains(customer.getName()));
    }

    @Test
    void exportOrdersToExcel() {
        // Если для экспорта должен быть создан хотя бы 1 заказ
        createTestOrder(1);
        ordersPage.clickExportButton();

        assertThat(ordersPage.isExportStarted()).isTrue();
    }
}
