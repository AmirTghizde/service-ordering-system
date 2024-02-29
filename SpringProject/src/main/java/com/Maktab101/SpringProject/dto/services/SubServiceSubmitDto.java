package com.Maktab101.SpringProject.dto.services;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubServiceSubmitDto {

    @NotBlank
    private String name;

    @Positive
    private double baseWage;

    private String description;

    @NotBlank
    private String mainServiceName;

}
