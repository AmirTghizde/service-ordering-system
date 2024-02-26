package com.Maktab101.SpringProject.dto.users;

import com.Maktab101.SpringProject.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewHistoryDto {
    private OrderStatus status;
}
