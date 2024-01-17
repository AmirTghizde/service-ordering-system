package ir.maktabSharif101.finalProject.repository.impl;

import ir.maktabSharif101.finalProject.base.repository.BaseEntityRepositoryImpl;
import ir.maktabSharif101.finalProject.entity.Suggestion;
import ir.maktabSharif101.finalProject.repository.SuggestionRepository;

import javax.persistence.EntityManager;

public class SuggestionRepositoryImpl extends BaseEntityRepositoryImpl<Suggestion,Long> implements SuggestionRepository {
    public SuggestionRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Suggestion> getEntityClass() {
        return Suggestion.class;
    }
}
