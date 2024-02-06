package com.Maktab101.SpringProject.service;

import com.Maktab101.SpringProject.dto.order.FinishOrderDto;
import com.Maktab101.SpringProject.dto.suggestion.SendSuggestionDto;
import com.Maktab101.SpringProject.model.Suggestion;

import java.util.List;

public interface OrderSuggestionService {
    void selectSuggestion(Long orderId,Long suggestionId);
    void sendSuggestion(Long technicianId, SendSuggestionDto sendSuggestionDto);
    List<Suggestion> getSuggestionByTechnicianPoint(Long orderId, boolean ascending);
    List<Suggestion> getSuggestionByPrice(Long orderId,boolean ascending);
    long isAfterSuggestedTime(Long orderId);
    void handelFinishOrder(FinishOrderDto dto);
}
