package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.Manager;
import com.Maktab101.SpringProject.repository.ManagerRepository;
import com.Maktab101.SpringProject.service.ManagerService;
import com.Maktab101.SpringProject.service.dto.RegisterDto;
import com.Maktab101.SpringProject.utils.CustomException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ManagerServiceImplTest {

    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private Validator validator;
    @InjectMocks
    private ManagerServiceImpl managerService;

    @BeforeEach
    void setUp() {
        managerService = new ManagerServiceImpl(managerRepository, validator);
    }



    @Test
    void testRegister_ValidInfo_ReturnsManager() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");
        Set<ConstraintViolation<RegisterDto>> violations = new HashSet<>();


        when(validator.validate(registerDto)).thenReturn(violations);
        Manager expectedManager = managerService.mapDtoValues(registerDto);

        when(managerRepository.save(any(Manager.class))).thenReturn(expectedManager);

        // When
        Manager actualManager = managerService.register(registerDto);

        // Then
        assertThat(actualManager).isEqualTo(expectedManager);
        verify(managerRepository).save(any(Manager.class));
        verify(managerRepository).existsByEmail(registerDto.getEmailAddress());
        verifyNoMoreInteractions(managerRepository);
    }
    @Test
    void testRegister_InvalidInfo_ThrowsException() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Aliiii");
        Set<ConstraintViolation<RegisterDto>> violations = new HashSet<>();
        ConstraintViolation<RegisterDto> mockedViolation1 = mock(ConstraintViolation.class);
        when(mockedViolation1.getMessage()).thenReturn("invalid Password");
        violations.add(mockedViolation1);
        when(validator.validate(registerDto)).thenReturn(violations);

        // When/Then
        assertThatThrownBy(() -> managerService.register(registerDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("(×_×;）\n" +
                        "❗ERROR: ValidationException\n" +
                        "\uD83D\uDCC3DESC:\n" +
                        "invalid Password");
        verifyNoInteractions(managerRepository);
    }
    @Test
    void testRegister_CatchesPersistenceException_WhenThrown() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");
        Set<ConstraintViolation<RegisterDto>> violations = new HashSet<>();

        when(validator.validate(registerDto)).thenReturn(violations);
        doThrow(new PersistenceException("PersistenceException Message")).when(managerRepository).save(any(Manager.class));

        // When/Then
        assertThatThrownBy(() -> managerService.register(registerDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("(×_×;）\n" +
                        "❗ERROR: PersistenceException\n" +
                        "\uD83D\uDCC3DESC:\n" +
                        "PersistenceException Message");

        verify(managerRepository).save(any(Manager.class));
        verify(managerRepository).existsByEmail(registerDto.getEmailAddress());
        verifyNoMoreInteractions(managerRepository);
    }

    @Test
    void testGetViolationMessages() {
        // Given
        Set<ConstraintViolation<RegisterDto>> violations = new HashSet<>();
        ConstraintViolation<RegisterDto> mockedViolation1 = mock(ConstraintViolation.class);
        when(mockedViolation1.getMessage()).thenReturn("Violation1");
        violations.add(mockedViolation1);

        ConstraintViolation<RegisterDto> mockedViolation2 = mock(ConstraintViolation.class);
        when(mockedViolation2.getMessage()).thenReturn("Violation2");
        violations.add(mockedViolation2);

        // When
        String violationMessages = managerService.getViolationMessages(violations);

        // Then
        assertThat(violationMessages).contains("Violation1", "Violation2");
        verifyNoInteractions(managerRepository);
    }

    @Test
    void testCheckCondition_ConditionsAreMet() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");

        when(managerService.existsByEmailAddress(registerDto.getEmailAddress())).thenReturn(false);

        // When
        managerService.checkCondition(registerDto);

        // Then
        verify(managerRepository).existsByEmail(registerDto.getEmailAddress());
        verifyNoMoreInteractions(managerRepository);
    }

    @Test
    void testCheckCondition_ConditionsNotMet_ThrowsException() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");

        when(managerService.existsByEmailAddress(registerDto.getEmailAddress())).thenReturn(true);

        // When/Then
        assertThatThrownBy(() -> managerService.checkCondition(registerDto))
                .isInstanceOf(CustomException.class);
        verify(managerRepository).existsByEmail(registerDto.getEmailAddress());
        verifyNoMoreInteractions(managerRepository);
    }

    @Test
    void testMapDtoValues_ReturnsManager() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");

        // When
        Manager manager = managerService.mapDtoValues(registerDto);

        // Then
        assertThat(manager).isNotNull();
        assertThat(manager.getFirstname()).isEqualTo(registerDto.getFirstname());
        assertThat(manager.getLastname()).isEqualTo(registerDto.getLastname());
        assertThat(manager.getEmail()).isEqualTo(registerDto.getEmailAddress());
        assertThat(manager.getPassword()).isEqualTo(registerDto.getPassword());
        assertThat(manager.getManagerCode()).isNotNull();
        verifyNoInteractions(managerRepository);
    }
}