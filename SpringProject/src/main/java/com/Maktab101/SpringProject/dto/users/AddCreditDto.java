package com.Maktab101.SpringProject.dto.users;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCreditDto {
    @NotNull
    Long customerId;
    @Positive
    double amount;
}
