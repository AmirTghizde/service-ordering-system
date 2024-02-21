package com.Maktab101.SpringProject.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.Customer}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDataDto implements Serializable {
    private String firstname;
    private String lastname;
    private String email;
    private double balance;
}