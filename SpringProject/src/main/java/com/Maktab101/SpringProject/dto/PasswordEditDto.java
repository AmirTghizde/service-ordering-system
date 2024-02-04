package com.Maktab101.SpringProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordEditDto {
    @NotNull
    private Long userId;
    @NotNull
    private String newPassword;
}
