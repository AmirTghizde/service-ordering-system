package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.service.dto.SuggestionDto;

public interface SuggestionService  {

    Suggestion findById(Long suggestionId);
    Suggestion save(Suggestion suggestion);
}
