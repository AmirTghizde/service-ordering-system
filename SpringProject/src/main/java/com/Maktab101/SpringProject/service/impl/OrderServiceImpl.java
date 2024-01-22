package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.*;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.repository.OrderRepository;
import com.Maktab101.SpringProject.service.*;
import com.Maktab101.SpringProject.service.dto.OrderSubmitDto;
import com.Maktab101.SpringProject.utils.CustomException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final SubServicesService subServicesService;
    private final SuggestionService suggestionService;
    private final CustomerService customerService;
    private final OrderRepository orderRepository;
    private final TechnicianService technicianService;
    private final Validator validator;

    @Autowired
    public OrderServiceImpl(SubServicesService subServicesService, SuggestionService suggestionService,
                            CustomerService customerService, OrderRepository orderRepository,
                            TechnicianService technicianService, Validator validator) {
        this.subServicesService = subServicesService;
        this.suggestionService = suggestionService;
        this.customerService = customerService;
        this.orderRepository = orderRepository;
        this.technicianService = technicianService;
        this.validator = validator;
    }


    @Override
    @Transactional
    public void submitOrder(Long customerId, OrderSubmitDto orderSubmitDto) {
        log.info("Customer with id [{}] is trying to submit a new order [{}]", customerId, orderSubmitDto);
        Set<ConstraintViolation<OrderSubmitDto>> violations = validator.validate(orderSubmitDto);
        if (violations.isEmpty()) {
            log.info("Information is validated - commencing registration");
            SubServices subServices = subServicesService.findById(orderSubmitDto.getSubServiceId());

            Customer customer = customerService.findById(customerId);

            checkCondition(orderSubmitDto, subServices);
            Order order = mapDtoValues(orderSubmitDto);

            try {
                log.info("Connecting to [{}]", orderRepository);
                customer.getOrders().add(order);
                subServices.getOrders().add(order);
                order.setSubServices(subServices);
                order.setCustomer(customer);
                customerService.save(customer);
                orderRepository.save(order);
                subServicesService.save(subServices);
                return;
            } catch (PersistenceException e) {
                System.out.println(e.getMessage());
            }
        }
        String violationMessages = getViolationMessages(violations);
        throw new CustomException("ValidationException", violationMessages);
    }

    @Override
    @Transactional
    public List<Order> findAwaitingOrdersByTechnician(Long technicianId) {
        Technician technician = technicianService.findById(technicianId);

        List<SubServices> subServices = technician.getSubServices();

        List<OrderStatus> orderStatuses = Arrays.asList(
                OrderStatus.AWAITING_TECHNICIAN_SUGGESTION,
                OrderStatus.AWAITING_TECHNICIAN_SELECTION
        );
        return orderRepository.findBySubServicesInAndOrderStatusIn(subServices,orderStatuses);
    }

    @Override
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()->
                new CustomException("OrderNotFound", "We can not find the order"));
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Suggestion> getSuggestionByTechnicianPoint(Long orderId, boolean ascending) {
        Order order = findById(orderId);
        List<Suggestion> suggestions = order.getSuggestions();
        Comparator<Suggestion> scoreComparing = Comparator.comparingDouble(s -> s.getTechnician().getScore());
        if (!ascending) {
            scoreComparing = scoreComparing.reversed();
        }
        suggestions.sort(scoreComparing);
        return suggestions;
    }

    @Override
    @Transactional
    public List<Suggestion> getSuggestionByPrice(Long orderId, boolean ascending) {
        Order order = findById(orderId);
        List<Suggestion> suggestions = order.getSuggestions();
        Comparator<Suggestion> priceComparing = Comparator.comparing(Suggestion::getSuggestedPrice);
        if (!ascending){
            priceComparing = priceComparing.reversed();
        }
        suggestions.sort(priceComparing);
        return suggestions;
    }

    @Override
    @Transactional
    public void selectSugestion(Long orderId, Long suggestionId) {
        Suggestion suggestion = suggestionService.findById(suggestionId);
        Order order = findById(orderId);

        if (!order.getSuggestions().contains(suggestion)){
            throw new CustomException("InvalidSuggestion","No suggestion found with that info");
        } else if (!order.getOrderStatus().equals(OrderStatus.AWAITING_TECHNICIAN_SELECTION)) {
            throw new CustomException("InvalidAction","You can't select a suggestion");
        }

        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_ARRIVAL);
        orderRepository.save(order);
    }

    @Override
    public void startOrder(Long orderId) {
        Order order = findById(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.AWAITING_TECHNICIAN_ARRIVAL)){
            throw new CustomException("InvalidAction","You can't start an order");
        }
        order.setOrderStatus(OrderStatus.STARTED);
        save(order);
    }

    @Override
    public void finishOrder(Long orderId) {
        Order order = findById(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.STARTED)){
            throw new CustomException("InvalidAction","You can't finish an order");
        }
        order.setOrderStatus(OrderStatus.FINISHED);
        save(order);
    }

    private String getViolationMessages(Set<ConstraintViolation<OrderSubmitDto>> violations) {
        log.error("SubmitOrderDto violates some fields throwing exception");
        StringBuilder messageBuilder = new StringBuilder();
        for (ConstraintViolation<OrderSubmitDto> violation : violations) {
            messageBuilder.append("\n").append(violation.getMessage());
        }
        return messageBuilder.toString().trim();
    }

    protected void checkCondition(OrderSubmitDto orderSubmitDto, SubServices subServices) {
        log.info("Checking order conditions");
        if (orderSubmitDto.getPrice() < subServices.getBaseWage()) {
            log.error("Price is lower than base wage throwing exception");
            throw new CustomException("InvalidPrice", "Price can't be lower than base wage");
        }
        LocalDateTime localDateTime = convertDateAndTime(orderSubmitDto.getTime(), orderSubmitDto.getDate());
        LocalDateTime now = LocalDateTime.now();
        if (localDateTime.isBefore(now)) {
            log.error("Date is before now throwing exception");
            throw new CustomException("InvalidDateAndTime", "Date and time can't be before now");
        }
    }

    protected Order mapDtoValues(OrderSubmitDto orderSubmitDto) {
        log.info("Mapping [{}] values", orderSubmitDto);
        Order order = new Order();
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);
        order.setJobInfo(orderSubmitDto.getJobInfo());
        order.setAddress(orderSubmitDto.getAddress());
        order.setPrice(orderSubmitDto.getPrice());
        order.setPoint(0);

        LocalDateTime localDateTime = convertDateAndTime(orderSubmitDto.getTime(), orderSubmitDto.getDate());

        order.setDateAndTime(localDateTime);
        return order;
    }

    private LocalDateTime convertDateAndTime(String time, String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);

        return localDate.atTime(localTime);
    }

}
