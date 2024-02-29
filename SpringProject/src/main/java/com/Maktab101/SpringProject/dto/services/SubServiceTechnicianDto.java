package com.Maktab101.SpringProject.dto.services;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubServiceTechnicianDto {
    @NotNull
    private Long technicianId;
    @NotNull
    private Long subServiceId;
}
