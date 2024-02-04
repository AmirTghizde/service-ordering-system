package com.Maktab101.SpringProject.dto.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.SubServices}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubServicesDto implements Serializable {
    private Long id;
    private String name;
    private double baseWage;
    private String description;
}