package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.repository.CustomerRepository;
import com.Maktab101.SpringProject.dto.RegisterDto;
import com.Maktab101.SpringProject.utils.CustomException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private Validator validator;
    private CustomerServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerServiceImpl(customerRepository, validator);
    }


    @Test
    void testRegister_ValidInfo_ReturnsCustomer() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");

        Set<ConstraintViolation<RegisterDto>> violations = new HashSet<>();

        when(validator.validate(registerDto)).thenReturn(violations);
        Customer expectedCustomer = underTest.mapDtoValues(registerDto);

        when(customerRepository.save(any(Customer.class))).thenReturn(expectedCustomer);

        // When
        Customer actualCustomer = underTest.register(registerDto);

        // Then
        assertThat(actualCustomer).isEqualTo(expectedCustomer);
        verify(customerRepository).save(any(Customer.class));
        verify(customerRepository).existsByEmail(registerDto.getEmailAddress());
        verifyNoMoreInteractions(customerRepository);
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
        assertThatThrownBy(() -> underTest.register(registerDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("(×_×;）\n" +
                        "❗ERROR: ValidationException\n" +
                        "\uD83D\uDCC3DESC:\n" +
                        "invalid Password");
        verifyNoInteractions(customerRepository);
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
        doThrow(new PersistenceException("PersistenceException Message")).when(customerRepository).save(any(Customer.class));

        // When/Then
        assertThatThrownBy(() -> underTest.register(registerDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: PersistenceException
                        \uD83D\uDCC3DESC:
                        PersistenceException Message""");

        verify(customerRepository).save(any(Customer.class));
        verify(customerRepository).existsByEmail(registerDto.getEmailAddress());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testGetViolationMessages_ReturnsViolationMessage() {
        // Given
        Set<ConstraintViolation<RegisterDto>> violations = new HashSet<>();

        ConstraintViolation<RegisterDto> mockedViolation1 = mock(ConstraintViolation.class);
        when(mockedViolation1.getMessage()).thenReturn("Violation1");
        violations.add(mockedViolation1);

        ConstraintViolation<RegisterDto> mockedViolation2 = mock(ConstraintViolation.class);
        when(mockedViolation2.getMessage()).thenReturn("Violation2");
        violations.add(mockedViolation2);

        // When
        String violationMessages = underTest.getViolationMessages(violations);

        // Then
        assertThat(violationMessages).contains("Violation1", "Violation2");
        verifyNoInteractions(customerRepository);
    }

    @Test
    void testCheckCondition_ConditionsAreMet() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");

        when(customerRepository.existsByEmail(registerDto.getEmailAddress())).thenReturn(false);

        // When
        underTest.checkCondition(registerDto);

        // Then
        verify(customerRepository).existsByEmail(registerDto.getEmailAddress());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testCheckCondition_ConditionsNotMet_ThrowsException() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");

        when(customerRepository.existsByEmail(registerDto.getEmailAddress())).thenReturn(true);

        // When/Then
        assertThatThrownBy(() -> underTest.checkCondition(registerDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: DuplicateEmailAddress
                        \uD83D\uDCC3DESC:
                        Email address already exists in the database""");
        verify(customerRepository).existsByEmail(registerDto.getEmailAddress());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testMapDtoValues_ReturnsCustomer() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");

        // When
        Customer customer = underTest.mapDtoValues(registerDto);

        // Then
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstname()).isEqualTo(registerDto.getFirstname());
        assertThat(customer.getLastname()).isEqualTo(registerDto.getLastname());
        assertThat(customer.getEmail()).isEqualTo(registerDto.getEmailAddress());
        assertThat(customer.getPassword()).isEqualTo(registerDto.getPassword());
        verifyNoInteractions(customerRepository);
    }
}