package factory;

import models.Product;

import java.util.concurrent.ThreadLocalRandom;

public class ProductFactory {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public static Product defaultProduct() {
        // антипаттерн: data-clones
        // фабрику вместо копипасты конструкторов
        int randomNumber = random.nextInt(1, 5000);
        return Product.builder()
                .name("Тестовый товар-" + randomNumber)
                .price(randomNumber)
                .build();
    }
}
