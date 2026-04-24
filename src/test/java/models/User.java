package models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private String name;
    private String email;
    private UserRole role;
    private String phone;
    private String address;
    private PaymentMethod paymentMethod;
    private String cardNumber;
}
