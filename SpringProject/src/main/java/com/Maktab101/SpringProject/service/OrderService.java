package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.dto.users.CardPaymentDto;
import com.Maktab101.SpringProject.dto.users.RequestDto;
import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.dto.order.OrderSubmitDto;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface OrderService {
    void submitOrder(Long customerId, OrderSubmitDto orderSubmitDto);
    List<Order> findAwaitingOrdersByTechnician(Long technicianId);
    Order findById(Long orderId);
    Order save(Order order);
    void startOrder(Long orderId);
    void finishOrder(Long orderId,double point);
    void addComment(Long orderId,String comment);
    int getNumberCaptcha();
    List<Order>handelFiltering(RequestDto requestDto);
    void checkOrderOwner(Long currentUserId,Long orderId);

}
