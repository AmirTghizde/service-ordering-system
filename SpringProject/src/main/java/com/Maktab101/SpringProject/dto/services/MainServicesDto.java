package com.Maktab101.SpringProject.dto.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.MainServices}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainServicesDto implements Serializable {
    private Long id;
    private String name;
}