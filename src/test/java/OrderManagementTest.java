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

    private OrdersPage ordersPage;
    private User customer;
    private Product product;

    @BeforeEach
    void prepareData() {
        UserPage userPage = new UserPage(driver);
        ProductsPage productsPage = new ProductsPage(driver);
        ordersPage = new OrdersPage(driver);

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
        ordersPage.clickCreateOrder();
        ordersPage.fillCustomer(customer);
        ordersPage.fillPayment(customer);
        ordersPage.selectFirstProduct(product.getName());
        ordersPage.setQuantity(quantity);
        ordersPage.submitOrder();
        return ordersPage.getOrderId();
    }

    @Test
    void createOrderAndVerifyAllFields() {
        int quantity = 3;
        String orderId = createTestOrder(quantity);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(ordersPage.getDetailCustomerName())
                .as("Имя заказчика")
                .isEqualTo(customer.getName());

        soft.assertThat(ordersPage.getDetailCustomerPhone())
                .as("Телефон заказчика")
                .isEqualTo(customer.getPhone());

        soft.assertThat(ordersPage.getDetailCustomerAddress())
                .as("Адрес доставки")
                .isEqualTo(customer.getAddress());

        soft.assertThat(ordersPage.getDetailQuantity())
                .as("Количество товара")
                .isEqualTo(quantity);

        soft.assertThat(ordersPage.getDetailTotal())
                .as("Итоговая сумма заказа")
                .isEqualTo(product.getFormattedPrice(quantity));

        soft.assertThat(ordersPage.getOrderStatus(orderId))
                .as("Статус заказа")
                .isEqualTo(OrderStatus.PENDING);

        soft.assertThat(ordersPage.isCreatedAtDisplayed())
                .as("Отображение даты создания")
                .isTrue();

        soft.assertThat(ordersPage.isErrorBlockDisplayed())
                .as("Отсутствие блока с ошибками")
                .isFalse();

        soft.assertAll();
    }

    @Test
    void approveCreatedOrder() {
        String orderId = createTestOrder(1);
        ordersPage.clickApprove(orderId);

        assertThat(ordersPage.getOrderStatus(orderId)).isEqualTo(OrderStatus.APPROVED);
    }

    @Test
    void cancelCreatedOrder() {
        String orderId = createTestOrder(1);
        ordersPage.clickCancel(orderId);

        assertThat(ordersPage.getOrderStatus(orderId)).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void searchOrdersByCustomer() {
        createTestOrder(1);
        ordersPage.searchOrderByCustomer(customer.getName());
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
