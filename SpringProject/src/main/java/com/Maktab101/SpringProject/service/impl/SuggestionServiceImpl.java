package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.repository.SuggestionRepository;
import com.Maktab101.SpringProject.service.SuggestionService;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import com.Maktab101.SpringProject.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SuggestionServiceImpl implements SuggestionService {
    private final SuggestionRepository suggestionRepository;


    @Autowired
    public SuggestionServiceImpl(SuggestionRepository suggestionRepository) {
        this.suggestionRepository = suggestionRepository;
    }
    @Override
    public Suggestion findById(Long suggestionId) {
        return suggestionRepository.findById(suggestionId).orElseThrow(
                ()->new NotFoundException("Suggestion: "+suggestionId)
        );
    }

    @Override
    public Suggestion save(Suggestion suggestion) {
        return suggestionRepository.save(suggestion);
    }
}
