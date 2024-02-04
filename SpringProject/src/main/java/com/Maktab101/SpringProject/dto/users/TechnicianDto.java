package com.Maktab101.SpringProject.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.Technician}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianDto implements Serializable {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private double score;
}