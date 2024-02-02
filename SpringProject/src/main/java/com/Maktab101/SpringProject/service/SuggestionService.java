package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.Suggestion;

public interface SuggestionService  {

    Suggestion findById(Long suggestionId);
    Suggestion save(Suggestion suggestion);
}
