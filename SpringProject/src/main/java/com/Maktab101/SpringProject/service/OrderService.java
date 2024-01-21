package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.service.dto.OrderSubmitDto;

public interface OrderService {
    void submitOrder(Long customerId, OrderSubmitDto orderSubmitDto);
}
