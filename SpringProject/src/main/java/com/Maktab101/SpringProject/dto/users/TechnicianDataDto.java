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
public class TechnicianDataDto implements Serializable {
    private String firstname;
    private String lastname;
    private String email;
    private double score;
    private double balance;
    private byte[] imageData;
}