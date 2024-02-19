package com.Maktab101.SpringProject.dto.order;

import com.Maktab101.SpringProject.dto.services.SubServicesDto;
import com.Maktab101.SpringProject.dto.suggestion.SuggestionDto;
import com.Maktab101.SpringProject.dto.users.CustomerDto;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.Order}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto implements Serializable {
    private Long id;
    private OrderStatus orderStatus;
    private String jobInfo;
    private LocalDateTime dateAndTime;
    private String Address;
    private double price;
    private SuggestionDto SelectedSuggestion;
    private CustomerDto customer;
    private SubServicesDto subServices;
}