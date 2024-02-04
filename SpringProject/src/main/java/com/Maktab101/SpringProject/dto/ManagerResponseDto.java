package com.Maktab101.SpringProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.Manager}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerResponseDto implements Serializable {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String managerCode;
}