package com.Maktab101.SpringProject.dto.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.MainServices}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainServicesResponseDto implements Serializable {
    private Long id;
    private String name;
    private List<SubServicesDto> subServices = new ArrayList<>();
}