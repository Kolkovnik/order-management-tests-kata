package factory;
import models.User;
import models.UserRole;
import models.PaymentMethod;
import java.util.concurrent.ThreadLocalRandom;

public class UserFactory {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public static User defaultCustomer() {
        // антипаттерн: mystery-guest
        // все данные создаются через фабрику, а не где-то на стороне
        return User.builder()
                .name("Иван Петров")
                .email("ivan_" + random.nextInt(1000, 9999) + "@test.com")
                .role(UserRole.CUSTOMER)
                .phone("+7999" + random.nextLong(1000000, 10000000))
                .address("Москва, Тверская, 1")
                .paymentMethod(PaymentMethod.CARD)
                .cardNumber(String.valueOf(random.nextLong(1000000000000000L, 10000000000000000L)))
                .build();
    }
}
