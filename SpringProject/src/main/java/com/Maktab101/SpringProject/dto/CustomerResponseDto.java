package com.Maktab101.SpringProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.Customer}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto implements Serializable {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private double balance = 0;
    private List<OrderDto> orders;
}