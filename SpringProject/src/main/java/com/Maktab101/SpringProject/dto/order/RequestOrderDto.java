package com.Maktab101.SpringProject.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderDto {

    Long orderId;
    int captcha;
    double amount;

}
