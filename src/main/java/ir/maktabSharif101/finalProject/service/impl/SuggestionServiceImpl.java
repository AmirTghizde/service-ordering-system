package ir.maktabSharif101.finalProject.service.impl;


import ir.maktabSharif101.finalProject.base.service.BaseEntityServiceImpl;
import ir.maktabSharif101.finalProject.entity.Suggestion;
import ir.maktabSharif101.finalProject.repository.SuggestionRepository;
import ir.maktabSharif101.finalProject.service.SuggestionService;

public class SuggestionServiceImpl extends BaseEntityServiceImpl<Suggestion, Long, SuggestionRepository>
        implements SuggestionService {
    public SuggestionServiceImpl(SuggestionRepository baseRepository) {
        super(baseRepository);
    }
}
