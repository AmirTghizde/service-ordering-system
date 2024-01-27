package com.Maktab101.SpringProject.service.impl;


import com.Maktab101.SpringProject.model.*;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.service.OrderService;
import com.Maktab101.SpringProject.service.SubServicesService;
import com.Maktab101.SpringProject.service.SuggestionService;
import com.Maktab101.SpringProject.service.TechnicianService;
import com.Maktab101.SpringProject.service.dto.SuggestionDto;
import com.Maktab101.SpringProject.utils.CustomException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderSuggestionImplTest {
    @Mock
    private OrderService orderService;
    @Mock
    private SuggestionService suggestionService;
    @Mock
    private SubServicesService subServicesService;
    @Mock
    private TechnicianService technicianService;
    @Mock
    private Validator validator;
    private OrderSuggestionImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new OrderSuggestionImpl(
                orderService, suggestionService, subServicesService, technicianService, validator
        );
    }

    @Test
    void testSelectSuggestion_ValidInfo_ChangesOrderStatues() {
        // Given
        Long suggestionId = 1L;
        Long orderId = 2L;
        Suggestion suggestion = new Suggestion();
        suggestion.setId(suggestionId);

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SELECTION);
        order.getSuggestions().add(suggestion);

        when(orderService.findById(orderId)).thenReturn(order);
        when(suggestionService.findById(suggestionId)).thenReturn(suggestion);

        // When
        underTest.selectSuggestion(orderId,suggestionId);

        // Then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.AWAITING_TECHNICIAN_ARRIVAL);
        verify(orderService).findById(orderId);
        verify(suggestionService).findById(suggestionId);
        verify(orderService).save(order);
        verifyNoMoreInteractions(orderService);
        verifyNoMoreInteractions(suggestionService);
    }
    @Test
    void testSelectSuggestion_InvalidSuggestion_ThrowsException() {
        // Given
        Long suggestionId = 1L;
        Long orderId = 2L;
        Suggestion suggestion = new Suggestion();
        suggestion.setId(suggestionId);

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SELECTION);

        when(orderService.findById(orderId)).thenReturn(order);
        when(suggestionService.findById(suggestionId)).thenReturn(suggestion);

        // When
        assertThatThrownBy(()->underTest.selectSuggestion(orderId,suggestionId))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: InvalidSuggestion
                        \uD83D\uDCC3DESC:
                        We can't find that suggestion in your order""");

        // Then
        assertThat(order.getOrderStatus()).isNotEqualTo(OrderStatus.AWAITING_TECHNICIAN_ARRIVAL);
        verify(orderService).findById(orderId);
        verify(suggestionService).findById(suggestionId);
        verifyNoMoreInteractions(orderService);
        verifyNoMoreInteractions(suggestionService);
    }
    @Test
    void testSelectSuggestion_InvalidStatus_ThrowsException() {
        // Given
        Long suggestionId = 1L;
        Long orderId = 2L;
        Suggestion suggestion = new Suggestion();
        suggestion.setId(suggestionId);

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.FINISHED);
        order.getSuggestions().add(suggestion);

        when(orderService.findById(orderId)).thenReturn(order);
        when(suggestionService.findById(suggestionId)).thenReturn(suggestion);

        // When
        assertThatThrownBy(()->underTest.selectSuggestion(orderId,suggestionId))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: InvalidAction
                        \uD83D\uDCC3DESC:
                        You can't select suggestions anymore""");

        // Then
        assertThat(order.getOrderStatus()).isNotEqualTo(OrderStatus.AWAITING_TECHNICIAN_ARRIVAL);
        verify(orderService).findById(orderId);
        verify(suggestionService).findById(suggestionId);
        verifyNoMoreInteractions(orderService);
        verifyNoMoreInteractions(suggestionService);
    }

    @Test
    void testSendSuggestion_ValidInfo_SavesSuggestion() {
        // Given
        ArgumentCaptor<Suggestion> suggestionCaptor = ArgumentCaptor.forClass(Suggestion.class);

        Long orderId = 1L;
        Long technicianId = 2L;
        Set<ConstraintViolation<SuggestionDto>> violations =new HashSet<>();
        SuggestionDto dto = new SuggestionDto();
        dto.setOrderID(1L);
        dto.setDuration("00:56");
        dto.setSuggestedDate("2025-01-20");
        dto.setSuggestedTime("12:00");
        dto.setSuggestedPrice(500);

        Technician technician = new Technician();
        technician.setId(technicianId);
        technician.setEmail("Ali@gmail.com");
        technician.setEmail("Ali1234");

        SubServices subServices = new SubServices();
        subServices.setId(3L);
        subServices.setName("HouseCleaning");
        subServices.getTechnicians().add(technician);
        subServices.setBaseWage(20);

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);
        order.setSubServices(subServices);


        when(validator.validate(dto)).thenReturn(violations);
        when(orderService.findById(orderId)).thenReturn(order);
        when(subServicesService.findById(order.getSubServices().getId())).thenReturn(subServices);
        when(technicianService.findById(technicianId)).thenReturn(technician);

        // When
        underTest.sendSuggestion(technicianId,dto);
        verify(suggestionService).save(suggestionCaptor.capture());

        // Then
        Suggestion savedSuggestion = suggestionCaptor.getValue();
        assertThat(order.getSuggestions()).contains(savedSuggestion);
        assertThat(savedSuggestion.getOrder()).isEqualTo(order);
        verify(orderService).findById(orderId);
        verify(subServicesService).findById(order.getSubServices().getId());
        verify(technicianService).findById(technicianId);
        verify(orderService).save(order);
        verify(suggestionService).save(savedSuggestion);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.AWAITING_TECHNICIAN_SELECTION);
    }
    @Test
    void testSendSuggestion_InvalidInfo_ThrowsException() {
        // Given
        Long orderId = 1L;
        Long technicianId = 2L;

        Set<ConstraintViolation<SuggestionDto>> violations =new HashSet<>();
        ConstraintViolation<SuggestionDto> mockedViolation1 = mock(ConstraintViolation.class);
        when(mockedViolation1.getMessage()).thenReturn("invalid Date");
        violations.add(mockedViolation1);

        SuggestionDto dto = new SuggestionDto();
        dto.setOrderID(1L);
        dto.setDuration("00:56");
        dto.setSuggestedDate("2025-01-20");
        dto.setSuggestedTime("12:00");
        dto.setSuggestedPrice(500);

        Technician technician = new Technician();
        technician.setId(technicianId);
        technician.setEmail("Ali@gmail.com");
        technician.setEmail("Ali1234");

        SubServices subServices = new SubServices();
        subServices.setId(3L);
        subServices.setName("HouseCleaning");
        subServices.getTechnicians().add(technician);
        subServices.setBaseWage(20);

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);
        order.setSubServices(subServices);

        when(validator.validate(dto)).thenReturn(violations);

        // When/Then
        assertThatThrownBy(() -> underTest.sendSuggestion(technicianId,dto))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: ValidationException
                        \uD83D\uDCC3DESC:
                        invalid Date""");

       verifyNoInteractions(orderService);
       verifyNoInteractions(subServicesService);
       verifyNoInteractions(technicianService);
       verifyNoInteractions(suggestionService);
    }
    @Test
    void testSendSuggestion_CatchesPersistenceException_WhenThrown() {
        // Given
        Long orderId = 1L;
        Long technicianId = 2L;
        Set<ConstraintViolation<SuggestionDto>> violations =new HashSet<>();
        SuggestionDto dto = new SuggestionDto();
        dto.setOrderID(1L);
        dto.setDuration("00:56");
        dto.setSuggestedDate("2025-01-20");
        dto.setSuggestedTime("12:00");
        dto.setSuggestedPrice(500);

        Technician technician = new Technician();
        technician.setId(technicianId);
        technician.setEmail("Ali@gmail.com");
        technician.setEmail("Ali1234");

        SubServices subServices = new SubServices();
        subServices.setId(3L);
        subServices.setName("HouseCleaning");
        subServices.getTechnicians().add(technician);
        subServices.setBaseWage(20);

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);
        order.setSubServices(subServices);


        when(validator.validate(dto)).thenReturn(violations);
        when(orderService.findById(orderId)).thenReturn(order);
        when(subServicesService.findById(order.getSubServices().getId())).thenReturn(subServices);
        when(technicianService.findById(technicianId)).thenReturn(technician);
        doThrow(new PersistenceException("PersistenceException Message"))
                .when(suggestionService).save(any(Suggestion.class));

        // When/Then
        assertThatThrownBy(() -> underTest.sendSuggestion(technicianId,dto))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: PersistenceException
                        \uD83D\uDCC3DESC:
                        PersistenceException Message""");

        verify(orderService).findById(orderId);
        verify(subServicesService).findById(order.getSubServices().getId());
        verify(technicianService).findById(technicianId);
        verify(orderService).save(order);
        verify(suggestionService).save(any(Suggestion.class));
    }


    @Test
    void testGetSuggestionByTechnicianPoint_Ascending() {
        // Given
        boolean ascending = true;
        Long orderId = 1L;

        Technician technician1 = new Technician();
        technician1.setScore(10);
        Technician technician2 = new Technician();
        technician2.setScore(20);

        Suggestion suggestion1 = new Suggestion();
        suggestion1.setId(2L);
        suggestion1.setSuggestedPrice(50);
        suggestion1.setTechnician(technician1);
        Suggestion suggestion2 = new Suggestion();
        suggestion2.setId(3L);
        suggestion2.setSuggestedPrice(60);
        suggestion2.setTechnician(technician2);

        Order order = new Order();
        order.setId(orderId);
        order.getSuggestions().add(suggestion1);
        order.getSuggestions().add(suggestion2);

        when(orderService.findById(orderId)).thenReturn(order);

        // When
        List<Suggestion> suggestionList = underTest.getSuggestionByTechnicianPoint(orderId, ascending);

        // Then
        assertThat(suggestionList).containsExactly(suggestion1,suggestion2);
        verify(orderService).findById(orderId);
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }
    @Test
    void testGetSuggestionByTechnicianPoint_Descending() {
        // Given
        boolean ascending = false;
        Long orderId = 1L;

        Technician technician1 = new Technician();
        technician1.setScore(10);
        Technician technician2 = new Technician();
        technician2.setScore(20);

        Suggestion suggestion1 = new Suggestion();
        suggestion1.setId(2L);
        suggestion1.setSuggestedPrice(50);
        suggestion1.setTechnician(technician1);
        Suggestion suggestion2 = new Suggestion();
        suggestion2.setId(3L);
        suggestion2.setSuggestedPrice(60);
        suggestion2.setTechnician(technician2);

        Order order = new Order();
        order.setId(orderId);
        order.getSuggestions().add(suggestion1);
        order.getSuggestions().add(suggestion2);

        when(orderService.findById(orderId)).thenReturn(order);

        // When
        List<Suggestion> suggestionList = underTest.getSuggestionByTechnicianPoint(orderId, ascending);

        // Then
        assertThat(suggestionList).containsExactly(suggestion2,suggestion1);
        verify(orderService).findById(orderId);
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testGetSuggestionByPrice_Ascending() {
        // Given
        boolean ascending = true;
        Long orderId = 1L;
        Suggestion suggestion1 = new Suggestion();
        suggestion1.setId(2L);
        suggestion1.setSuggestedPrice(50);
        Suggestion suggestion2 = new Suggestion();
        suggestion2.setId(3L);
        suggestion2.setSuggestedPrice(60);

        Order order = new Order();
        order.setId(orderId);
        order.getSuggestions().add(suggestion1);
        order.getSuggestions().add(suggestion2);

        when(orderService.findById(orderId)).thenReturn(order);

        // When
        List<Suggestion> suggestionList = underTest.getSuggestionByPrice(orderId, ascending);

        // Then
        assertThat(suggestionList).containsExactly(suggestion1,suggestion2);
        verify(orderService).findById(orderId);
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testGetSuggestionByPrice_Descending() {
        // Given
        boolean ascending = false;
        Long orderId = 1L;
        Suggestion suggestion1 = new Suggestion();
        suggestion1.setId(2L);
        suggestion1.setSuggestedPrice(50);
        Suggestion suggestion2 = new Suggestion();
        suggestion2.setId(3L);
        suggestion2.setSuggestedPrice(60);

        Order order = new Order();
        order.setId(orderId);
        order.getSuggestions().add(suggestion1);
        order.getSuggestions().add(suggestion2);

        when(orderService.findById(orderId)).thenReturn(order);

        // When
        List<Suggestion> suggestionList = underTest.getSuggestionByPrice(orderId, ascending);

        // Then
        assertThat(suggestionList).containsExactly(suggestion2,suggestion1);
        verify(orderService).findById(orderId);
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testCheckCondition_ValidInfo_DoesNothing() {
        // Given
        double price = 50;
        String date = "2025-01-20";
        String time = "12:05";
        SuggestionDto dto = new SuggestionDto();
        dto.setOrderID(1L);
        dto.setDuration("00:56");
        dto.setSuggestedDate(date);
        dto.setSuggestedTime(time);
        dto.setSuggestedPrice(price);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);

        Technician technician = new Technician();
        technician.setEmail("Ali@gmail.com");
        technician.setEmail("Ali1234");

        SubServices subServices = new SubServices();
        subServices.setBaseWage(20);
        subServices.getTechnicians().add(technician);

        // When
        underTest.checkCondition(technician, dto, subServices, order);

        // Then
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(orderService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testCheckCondition_invalidOrderStatus_ThrowsException() {
        // Given
        double price = 50;
        String date = "2025-01-20";
        String time = "12:05";
        SuggestionDto dto = new SuggestionDto();
        dto.setOrderID(1L);
        dto.setDuration("00:56");
        dto.setSuggestedDate(date);
        dto.setSuggestedTime(time);
        dto.setSuggestedPrice(price);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.FINISHED);

        Technician technician = new Technician();
        technician.setEmail("Ali@gmail.com");
        technician.setEmail("Ali1234");

        SubServices subServices = new SubServices();
        subServices.setBaseWage(20);
        subServices.getTechnicians().add(technician);

        // When/Then
        assertThatThrownBy(() -> underTest.checkCondition(technician, dto, subServices, order))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: InvalidAction
                        \uD83D\uDCC3DESC:
                        You can't send a suggestion for this order""");
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(orderService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testCheckCondition_invalidTechnician_ThrowsException() {
        // Given
        double price = 50;
        String date = "2025-01-20";
        String time = "12:05";
        SuggestionDto dto = new SuggestionDto();
        dto.setOrderID(1L);
        dto.setDuration("00:56");
        dto.setSuggestedDate(date);
        dto.setSuggestedTime(time);
        dto.setSuggestedPrice(price);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);

        Technician technician = new Technician();
        technician.setEmail("Ali@gmail.com");
        technician.setEmail("Ali1234");

        SubServices subServices = new SubServices();
        subServices.setBaseWage(20);

        // When/Then
        assertThatThrownBy(() -> underTest.checkCondition(technician, dto, subServices, order))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: WrongSubService
                        \uD83D\uDCC3DESC:
                        You don't have this sub service""");
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(orderService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testCheckCondition_invalidPrice_ThrowsException() {
        // Given
        double price = 1;
        String date = "2025-01-20";
        String time = "12:05";
        SuggestionDto dto = new SuggestionDto();
        dto.setOrderID(1L);
        dto.setDuration("00:56");
        dto.setSuggestedDate(date);
        dto.setSuggestedTime(time);
        dto.setSuggestedPrice(price);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);

        Technician technician = new Technician();
        technician.setEmail("Ali@gmail.com");
        technician.setEmail("Ali1234");

        SubServices subServices = new SubServices();
        subServices.setBaseWage(20);
        subServices.getTechnicians().add(technician);

        // When/Then
        assertThatThrownBy(() -> underTest.checkCondition(technician, dto, subServices, order))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: InvalidPrice
                        \uD83D\uDCC3DESC:
                        Price can't be lower than base wage""");
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(orderService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testCheckCondition_invalidDate_ThrowsException() {
        // Given
        double price = 50;
        String date = "2022-01-20";
        String time = "12:05";
        SuggestionDto dto = new SuggestionDto();
        dto.setOrderID(1L);
        dto.setDuration("00:56");
        dto.setSuggestedDate(date);
        dto.setSuggestedTime(time);
        dto.setSuggestedPrice(price);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);

        Technician technician = new Technician();
        technician.setEmail("Ali@gmail.com");
        technician.setEmail("Ali1234");

        SubServices subServices = new SubServices();
        subServices.setBaseWage(20);
        subServices.getTechnicians().add(technician);

        // When/Then
        assertThatThrownBy(() -> underTest.checkCondition(technician, dto, subServices, order))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: InvalidDateAndTime
                        \uD83D\uDCC3DESC:
                        Date and time can't be before now""");
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(orderService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testGetViolationMessages_ReturnsViolationString() {
        // Given
        Set<ConstraintViolation<SuggestionDto>> violations = new HashSet<>();
        ConstraintViolation<SuggestionDto> mockedViolation1 = mock(ConstraintViolation.class);
        when(mockedViolation1.getMessage()).thenReturn("Violation1");
        violations.add(mockedViolation1);

        ConstraintViolation<SuggestionDto> mockedViolation2 = mock(ConstraintViolation.class);
        when(mockedViolation2.getMessage()).thenReturn("Violation2");
        violations.add(mockedViolation2);

        // When
        String violationMessages = underTest.getViolationMessages(violations);

        // Then
        assertThat(violationMessages).contains("Violation1", "Violation2");
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(orderService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testToLocalDateTime_ReturnsLocalDateTime() {
        // Given
        String date = "2025-01-20";
        String time = "12:05";

        LocalDateTime expectedDateTime = LocalDateTime.of(2025, 1, 20, 12, 5);

        // When
        LocalDateTime actualLocalDateTime = underTest.toLocalDateTime(time, date);

        // Then
        assertThat(actualLocalDateTime).isEqualTo(expectedDateTime);
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(orderService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testConvertTime_ReturnsLocalTime() {
        // Given
        String time = "12:05";

        LocalTime expectedTime = LocalTime.of(12, 5);

        // When
        LocalTime actualLocalTime = underTest.convertTime(time);

        // Then
        assertThat(actualLocalTime).isEqualTo(expectedTime);
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(orderService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testMapDtoValues_ReturnsSuggestion() {
        // Given
        Technician technician = new Technician();
        technician.setEmail("Ali@Gmail.com");
        technician.setPassword("Ali1234");

        SuggestionDto dto = new SuggestionDto();
        dto.setOrderID(1L);
        dto.setDuration("00:56");
        dto.setSuggestedDate("2025-01-20");
        dto.setSuggestedTime("12:00");
        dto.setSuggestedPrice(500);

        // When
        Suggestion suggestion = underTest.mapDtoValues(technician, dto);

        // Then
        assertThat(suggestion).isNotNull();
        assertThat(suggestion.getDuration()).isEqualTo(dto.getDuration());
        assertThat(suggestion.getSuggestedPrice()).isEqualTo(dto.getSuggestedPrice());
        LocalDateTime localDateTime = underTest.toLocalDateTime(dto.getSuggestedTime(), dto.getSuggestedDate());
        assertThat(suggestion.getSuggestedDate()).isEqualTo(localDateTime);
        verifyNoInteractions(suggestionService);
        verifyNoInteractions(orderService);
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
    }
}