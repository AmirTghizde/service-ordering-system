package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.*;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.repository.OrderRepository;
import com.Maktab101.SpringProject.service.CustomerService;
import com.Maktab101.SpringProject.service.SubServicesService;
import com.Maktab101.SpringProject.service.TechnicianService;
import com.Maktab101.SpringProject.dto.order.OrderSubmitDto;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private SubServicesService subServicesService;
    @Mock
    private CustomerService customerService;
    @Mock
    private TechnicianService technicianService;
    @Mock
    private Validator validator;
    private OrderServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new OrderServiceImpl(
                subServicesService, customerService, orderRepository, technicianService, validator
        );
    }

    @Test
    void testSubmitOrder_ValidInfo_SubmitsOrder() {
        // Given
        Long subServiceId = 1L;
        Long customerId = 2L;

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        List<Order> customerOrderList = new ArrayList<>();
        List<Order> subServiceOrderList = new ArrayList<>();

        OrderSubmitDto orderDto = new OrderSubmitDto();
        orderDto.setSubServiceId(subServiceId);
        orderDto.setJobInfo("InfoTest");
        orderDto.setDate("2025-01-20");
        orderDto.setTime("12:05");
        orderDto.setAddress("AddressTest");
        orderDto.setPrice(50);

        Set<ConstraintViolation<OrderSubmitDto>> violations = new HashSet<>();

        Order order = underTest.mapDtoValues(orderDto);
        order.setId(5L);
        customerOrderList.add(order);
        subServiceOrderList.add(order);

        SubServices subServices = new SubServices();
        subServices.setId(subServiceId);
        subServices.setName("HouseCleaning");
        subServices.setBaseWage(10);
        subServices.setOrders(subServiceOrderList);

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setEmail("Ali@gmail.com");
        customer.setPassword("Ali12345");
        customer.setOrders(customerOrderList);

        when(validator.validate(orderDto)).thenReturn(violations);
        when(subServicesService.findById(subServiceId)).thenReturn(subServices);
        when(customerService.findById(customerId)).thenReturn(customer);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        underTest.submitOrder(customerId, orderDto);
        verify(orderRepository).save(orderCaptor.capture());

        // Then
        Order savedOrder = orderCaptor.getValue();
        assertThat(customer.getOrders()).contains(savedOrder);
        assertThat(subServices.getOrders()).contains(savedOrder);
        assertThat(savedOrder.getSubServices()).isEqualTo(subServices);
        assertThat(savedOrder.getCustomer()).isEqualTo(customer);
        verify(customerService).findById(customerId);
        verify(subServicesService).findById(subServiceId);
        verify(validator).validate(orderDto);
        verify(orderRepository).save(savedOrder);
        verify(subServicesService).save(subServices);
        verify(customerService).save(customer);
        verifyNoMoreInteractions(subServicesService);
        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(orderRepository);
    }
    @Test
    void testSubmitOrder_invalidValidInfo_ThrowsException() {
        // Given
        Long subServiceId = 1L;
        Long customerId = 2L;

        List<Order> customerOrderList = new ArrayList<>();
        List<Order> subServiceOrderList = new ArrayList<>();

        Set<ConstraintViolation<OrderSubmitDto>> violations = new HashSet<>();
        ConstraintViolation<OrderSubmitDto> mockedViolation1 = mock(ConstraintViolation.class);
        when(mockedViolation1.getMessage()).thenReturn("invalid Date");
        violations.add(mockedViolation1);

        OrderSubmitDto orderDto = new OrderSubmitDto();
        orderDto.setSubServiceId(subServiceId);
        orderDto.setJobInfo("InfoTest");
        orderDto.setDate("2025-01-20");
        orderDto.setTime("12:05");
        orderDto.setAddress("AddressTest");
        orderDto.setPrice(50);

        Order order = underTest.mapDtoValues(orderDto);
        order.setId(5L);
        customerOrderList.add(order);
        subServiceOrderList.add(order);

        SubServices subServices = new SubServices();
        subServices.setId(subServiceId);
        subServices.setName("HouseCleaning");
        subServices.setBaseWage(10);
        subServices.setOrders(subServiceOrderList);

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setEmail("Ali@gmail.com");
        customer.setPassword("Ali12345");
        customer.setOrders(customerOrderList);

        when(validator.validate(orderDto)).thenReturn(violations);

        // When/Then
        assertThatThrownBy(() -> underTest.submitOrder(customerId,orderDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: ValidationException
                        \uD83D\uDCC3DESC:
                        invalid Date""");

        // Then
        verifyNoInteractions(technicianService);
        verifyNoInteractions(subServicesService);
        verifyNoInteractions(customerService);
        verifyNoInteractions(orderRepository);
    }
    @Test
    void testSubmitOrder_CatchesPersistenceException_WhenThrown() {
        // Given
        Long subServiceId = 1L;
        Long customerId = 2L;

        Set<ConstraintViolation<OrderSubmitDto>> violations = new HashSet<>();

        List<Order> customerOrderList = new ArrayList<>();
        List<Order> subServiceOrderList = new ArrayList<>();


        OrderSubmitDto orderDto = new OrderSubmitDto();
        orderDto.setSubServiceId(subServiceId);
        orderDto.setJobInfo("InfoTest");
        orderDto.setDate("2025-01-20");
        orderDto.setTime("12:05");
        orderDto.setAddress("AddressTest");
        orderDto.setPrice(50);

        Order order = underTest.mapDtoValues(orderDto);
        order.setId(5L);
        customerOrderList.add(order);
        subServiceOrderList.add(order);

        SubServices subServices = new SubServices();
        subServices.setId(subServiceId);
        subServices.setName("HouseCleaning");
        subServices.setBaseWage(10);
        subServices.setOrders(subServiceOrderList);

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setEmail("Ali@gmail.com");
        customer.setPassword("Ali12345");
        customer.setOrders(customerOrderList);

        when(validator.validate(orderDto)).thenReturn(violations);
        when(subServicesService.findById(subServiceId)).thenReturn(subServices);
        when(customerService.findById(customerId)).thenReturn(customer);
        doThrow(new PersistenceException("PersistenceException Message"))
                .when(orderRepository).save(any(Order.class));

        // When/Then
        assertThatThrownBy(() -> underTest.submitOrder(customerId,orderDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: PersistenceException
                        \uD83D\uDCC3DESC:
                        PersistenceException Message""");

        // Then

        verify(customerService).findById(customerId);
        verify(subServicesService).findById(subServiceId);
        verify(validator).validate(orderDto);
        verify(orderRepository).save(any(Order.class));
        verify(customerService).save(customer);
    }

    @Test
    void testFindAwaitingOrdersByTechnician_ReturnsOrderList() {
        // Given
        Long technicianId= 1L;

        Technician technician = new Technician();
        technician.setId(technicianId);
        technician.setEmail("Ali@gmail.com");
        technician.setPassword("Ali1234");
        List<SubServices> subServicesList = new ArrayList<>();
        SubServices subServices = new SubServices();
        subServices.setName("HouseCleaning");
        SubServices subServices1 = new SubServices();
        subServices1.setName("CompanyCleaning");
        subServicesList.add(subServices);
        subServicesList.add(subServices1);

        technician.setSubServices(subServicesList);

        List<Order> expectedOrders = new ArrayList<>();
        Order order1 = new Order();
        order1.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);
        Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SELECTION);
        expectedOrders.add(order1);
        expectedOrders.add(order2);

        List<OrderStatus> orderStatuses = Arrays.asList(
                OrderStatus.AWAITING_TECHNICIAN_SUGGESTION,
                OrderStatus.AWAITING_TECHNICIAN_SELECTION);

        when(technicianService.findById(technicianId)).thenReturn(technician);
        when(orderRepository.findBySubServicesInAndOrderStatusIn(subServicesList,orderStatuses))
                .thenReturn(expectedOrders);

        // When
        List<Order> actualOrders = underTest.findAwaitingOrdersByTechnician(technicianId);

        // Then
        assertThat(actualOrders).isEqualTo(expectedOrders);
        verify(technicianService).findById(technicianId);
        verify(orderRepository).findBySubServicesInAndOrderStatusIn(subServicesList,orderStatuses);
        verifyNoMoreInteractions(technicianService);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testFindById_IfFound_ReturnsOrder() {
        // Given
        Long orderId = 1L;
        Order expectedOrder = new Order();
        expectedOrder.setAddress("TestAddress");
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        // When
        Order actualOrder = underTest.findById(orderId);

        // Then
        assertThat(actualOrder).isEqualTo(expectedOrder);
        assertThat(actualOrder.getId()).isEqualTo(expectedOrder.getId());
        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }
    @Test
    void testFindById_IfNotFound_ThrowsException() {
        // Given
        Long orderId = 1L;
        Order expectedOrder = new Order();
        expectedOrder.setAddress("TestAddress");
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(()-> underTest.findById(orderId))
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: OrderNotFound
                        \uD83D\uDCC3DESC:
                        We can not find the order""");

        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testSave_ReturnsOrder() {
        // Given
        Order expectedOrder = new Order();
        expectedOrder.setAddress("TestAddress");
        when(orderRepository.save(expectedOrder)).thenReturn(expectedOrder);

        // When
        Order actualOrder = underTest.save(expectedOrder);

        // Then
        assertThat(actualOrder).isEqualTo(expectedOrder);
        verify(orderRepository).save(expectedOrder);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testStartOrder_ValidStatus_ChangesStatusToStarted() {
        // Given
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_ARRIVAL);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        underTest.startOrder(orderId);

        // Then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.STARTED);
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(order);
        verifyNoMoreInteractions(orderRepository);
    }
    @Test
    void testStartOrder_InvalidStatus_ThrowsException() {
        // Given
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When/Then
        assertThatThrownBy(()-> underTest.startOrder(orderId))
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: InvalidAction
                        \uD83D\uDCC3DESC:
                        You can't start this order""");
        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testFinishOrder_ValidStatus_ChangesStatusToFinished() {
//        // Given
//        Long orderId = 1L;
//        Order order = new Order();
//        order.setId(orderId);
//        order.setOrderStatus(OrderStatus.STARTED);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
//
//        // When
//        underTest.finishOrder(orderId);
//
//        // Then
//        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.FINISHED);
//        verify(orderRepository).findById(orderId);
//        verify(orderRepository).save(order);
//        verifyNoMoreInteractions(orderRepository);
    }
    @Test
    void testFinishOrder_InvalidStatus_ThrowsException() {
//        // Given
//        Long orderId = 1L;
//        Order order = new Order();
//        order.setId(orderId);
//        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_ARRIVAL);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
//
//        // When/Then
//        assertThatThrownBy(()-> underTest.finishOrder(orderId))
//                .hasMessage("""
//                        (×_×;）
//                        ❗ERROR: InvalidAction
//                        \uD83D\uDCC3DESC:
//                        You can't finish this order""");
//        verify(orderRepository).findById(orderId);
//        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void testGetViolationMessages() {
        // Given
        Set<ConstraintViolation<OrderSubmitDto>> violations = new HashSet<>();
        ConstraintViolation<OrderSubmitDto> mockedViolation1 = mock(ConstraintViolation.class);
        when(mockedViolation1.getMessage()).thenReturn("Violation1");
        violations.add(mockedViolation1);

        ConstraintViolation<OrderSubmitDto> mockedViolation2 = mock(ConstraintViolation.class);
        when(mockedViolation2.getMessage()).thenReturn("Violation2");
        violations.add(mockedViolation2);

        // When
        String violationMessages = underTest.getViolationMessages(violations);

        // Then
        assertThat(violationMessages).contains("Violation1", "Violation2");
        verifyNoInteractions(orderRepository);
    }

    @Test
    void testCheckCondition_ValidInfo_DoesNothing() {
        // Given
        double price = 50;
        String date = "2025-01-20";
        String time = "12:05";
        OrderSubmitDto orderDto = new OrderSubmitDto();
        orderDto.setJobInfo("InfoTest");
        orderDto.setDate(date);
        orderDto.setTime(time);
        orderDto.setAddress("AddressTest");
        orderDto.setPrice(price);

        SubServices subServices = new SubServices();
        subServices.setName("HouseCleaning");
        subServices.setBaseWage(10);

        // When
        underTest.checkCondition(orderDto, subServices);

        // Then
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testCheckCondition_InvalidPrice_ThrowsException() {
        // Given
        double price = 1;
        String date = "2025-01-20";
        String time = "12:05";
        OrderSubmitDto orderDto = new OrderSubmitDto();
        orderDto.setJobInfo("InfoTest");
        orderDto.setDate(date);
        orderDto.setTime(time);
        orderDto.setAddress("AddressTest");
        orderDto.setPrice(price);

        SubServices subServices = new SubServices();
        subServices.setName("HouseCleaning");
        subServices.setBaseWage(10);

        // When/Then
        assertThatThrownBy(() -> underTest.checkCondition(orderDto, subServices))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: InvalidPrice
                        \uD83D\uDCC3DESC:
                        Price can't be lower than base wage""");
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testCheckCondition_InvalidTime_ThrowsException() {
        // Given
        double price = 20;
        String date = "2020-01-20";
        String time = "12:05";
        OrderSubmitDto orderDto = new OrderSubmitDto();
        orderDto.setJobInfo("InfoTest");
        orderDto.setDate(date);
        orderDto.setTime(time);
        orderDto.setAddress("AddressTest");
        orderDto.setPrice(price);

        SubServices subServices = new SubServices();
        subServices.setName("HouseCleaning");
        subServices.setBaseWage(10);

        // When/Then
        assertThatThrownBy(() -> underTest.checkCondition(orderDto, subServices))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: InvalidDateAndTime
                        \uD83D\uDCC3DESC:
                        Date and time can't be before now""");
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(subServicesService);
    }

    @Test
    void testMapDtoValues_ReturnsOrder() {
        // Given
        OrderSubmitDto orderDto = new OrderSubmitDto();
        orderDto.setJobInfo("InfoTest");
        orderDto.setDate("2025-01-20");
        orderDto.setTime("12:05");
        orderDto.setAddress("AddressTest");
        orderDto.setPrice(50);

        // When
        Order order = underTest.mapDtoValues(orderDto);

        // Then
        LocalDateTime localDateTime = underTest.convertDateAndTime(orderDto.getTime(), orderDto.getDate());
        assertThat(order).isNotNull();
        assertThat(order.getJobInfo()).isEqualTo(orderDto.getJobInfo());
        assertThat(order.getDateAndTime()).isEqualTo(localDateTime);
        assertThat(order.getAddress()).isEqualTo(orderDto.getAddress());
        assertThat(order.getAddress()).isEqualTo(orderDto.getAddress());
        assertThat(order.getPrice()).isEqualTo(orderDto.getPrice());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void testConvertDateAndTime() {
        // Given
        String date = "2025-01-20";
        String time = "12:05";

        LocalDateTime expectedDateTime = LocalDateTime.of(2025, 1, 20, 12, 5);

        // When
        LocalDateTime actualLocalDateTime = underTest.convertDateAndTime(time, date);

        // Then
        assertThat(actualLocalDateTime).isEqualTo(expectedDateTime);
    }
}