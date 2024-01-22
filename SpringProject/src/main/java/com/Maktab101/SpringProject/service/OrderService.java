package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.service.dto.OrderSubmitDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void submitOrder(Long customerId, OrderSubmitDto orderSubmitDto);
    List<Order> findAwaitingOrdersByTechnician(Long technicianId);
    Order findById(Long orderId);
    Order save(Order order);
    List<Suggestion> getSuggestionByTechnicianPoint(Long orderId,boolean ascending);
    List<Suggestion> getSuggestionByPrice(Long orderId,boolean ascending);
    void selectSugestion(Long orderId,Long suggestionId);
    void startOrder(Long orderId);
    void finishOrder(Long orderId);
}
