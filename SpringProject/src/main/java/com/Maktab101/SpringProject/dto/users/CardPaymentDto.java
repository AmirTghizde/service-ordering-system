package com.Maktab101.SpringProject.dto.users;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentDto {

    @NotBlank
    @Size(min = 16)
    String cardNumber;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+$",message = "Can't use numbers in card holder name")
    String cardHolder;
    @NotBlank
    String expirationMonth;
    @NotBlank
    String expirationYear;
    @NotBlank
    String cvv2;
    @NotBlank
    String captcha;
    @NotNull
    double amount;
    @NotNull
    Long orderId;

}
