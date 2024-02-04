package com.Maktab101.SpringProject.dto.users;

import com.Maktab101.SpringProject.dto.services.SubServicesDto;
import com.Maktab101.SpringProject.model.enums.TechnicianStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.Technician}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianResponseDto implements Serializable {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private TechnicianStatus status;
    private double score;
    private double balance;
    private List<SubServicesDto> subServices = new ArrayList<>();
}