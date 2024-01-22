package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.service.dto.SuggestionDto;

public interface SuggestionService  {
    void sendSuggestion(Long technicianId, SuggestionDto suggestionDto);
    Suggestion findById(Long suggestionId);
}
