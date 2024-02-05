package com.Maktab101.SpringProject.dto.order;

import com.Maktab101.SpringProject.dto.users.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.Order}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCommentDto implements Serializable {
    private Long id;
    private String comment;
    private double point;
    private CustomerDto customer;
}