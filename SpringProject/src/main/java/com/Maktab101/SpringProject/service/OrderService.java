package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.dto.order.OrderSubmitDto;

import java.util.List;

public interface OrderService {
    void submitOrder(Long customerId, OrderSubmitDto orderSubmitDto);
    List<Order> findAwaitingOrdersByTechnician(Long technicianId);
    Order findById(Long orderId);
    Order save(Order order);
    void startOrder(Long orderId);
    void finishOrder(Long orderId);
}
