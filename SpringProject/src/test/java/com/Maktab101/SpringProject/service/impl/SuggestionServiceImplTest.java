package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.MainServices;
import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.repository.SuggestionRepository;
import com.Maktab101.SpringProject.utils.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuggestionServiceImplTest {
    @Mock
    private SuggestionRepository suggestionRepository;

    private SuggestionServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new SuggestionServiceImpl(suggestionRepository);
    }

    @Test
    void testFindById_ReturnsSuggestion() {
        // Given
        Long id = 1L;
        Suggestion excpectedSuggestion = new Suggestion();
        excpectedSuggestion.setId(id);
        when(suggestionRepository.findById(id)).thenReturn(Optional.of(excpectedSuggestion));

        // When
        Suggestion actualSuggestion = underTest.findById(id);

        // Then
        assertThat(actualSuggestion).isEqualTo(excpectedSuggestion);
        assertThat(actualSuggestion.getId()).isEqualTo(id);
        verify(suggestionRepository).findById(id);
        verifyNoMoreInteractions(suggestionRepository);
    }
    @Test
    void testFindById_IfNotFound_ThrowsException() {
        // Given
        Long id = 1L;
        Suggestion excpectedSuggestion = new Suggestion();
        excpectedSuggestion.setId(id);
        when(suggestionRepository.findById(id)).thenReturn(Optional.empty());

        // When
        assertThatThrownBy(()-> underTest.findById(id))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: SuggestionNotFound
                        \uD83D\uDCC3DESC:
                        We can't find that suggestion""");
        verify(suggestionRepository).findById(id);
        verifyNoMoreInteractions(suggestionRepository);
    }

    @Test
    void testSave_ReturnsSuggestion() {
        // Given
        Suggestion excpectedSuggestion = new Suggestion();
        excpectedSuggestion.setSuggestedPrice(50);
        when(suggestionRepository.save(excpectedSuggestion)).thenReturn(excpectedSuggestion);

        // When
        Suggestion actualSuggestion = underTest.save(excpectedSuggestion);

        // Then
        assertThat(actualSuggestion).isEqualTo(excpectedSuggestion);
        verify(suggestionRepository).save(excpectedSuggestion);
        verifyNoMoreInteractions(suggestionRepository);
    }
}