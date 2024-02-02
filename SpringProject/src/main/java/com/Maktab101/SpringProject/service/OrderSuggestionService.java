package com.Maktab101.SpringProject.service;

import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.dto.SuggestionDto;

import java.util.List;

public interface OrderSuggestionService {
    void selectSuggestion(Long orderId,Long suggestionId);
    void sendSuggestion(Long technicianId, SuggestionDto suggestionDto);
    List<Suggestion> getSuggestionByTechnicianPoint(Long orderId, boolean ascending);
    List<Suggestion> getSuggestionByPrice(Long orderId,boolean ascending);
}
