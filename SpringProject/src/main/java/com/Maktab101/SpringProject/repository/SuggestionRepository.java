package com.Maktab101.SpringProject.repository;

import com.Maktab101.SpringProject.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository extends JpaRepository<Suggestion,Long> {
}
