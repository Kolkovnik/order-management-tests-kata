package models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Product {
    private String name;
    private int price;

    public String getFormattedPrice(int qty) {
        return String.format("%, d ₽", this.price * qty).replace(',', ' ');
    }
}
