package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.service.dto.OrderSubmitDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void submitOrder(Long customerId, OrderSubmitDto orderSubmitDto);
    List<Order> findAwaitingOrdersByTechnician(Long technicianId);
    Order findById(Long orderId);
    Order save(Order order);
}