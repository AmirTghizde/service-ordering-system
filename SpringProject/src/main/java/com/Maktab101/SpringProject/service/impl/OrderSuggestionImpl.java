package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.dto.suggestion.SendSuggestionDto;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.model.SubServices;
import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.service.*;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import com.Maktab101.SpringProject.utils.exceptions.NotFoundException;
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
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class OrderSuggestionImpl implements OrderSuggestionService {
    private final OrderService orderService;
    private final SuggestionService suggestionService;
    private final SubServicesService subServicesService;
    private final TechnicianService technicianService;
    private final Validator validator;

    @Autowired
    public OrderSuggestionImpl(OrderService orderService, SuggestionService suggestionService,
                               SubServicesService subServicesService, TechnicianService technicianService,
                               Validator validator) {
        this.orderService = orderService;
        this.suggestionService = suggestionService;
        this.subServicesService = subServicesService;
        this.technicianService = technicianService;
        this.validator = validator;
    }

    @Override
    @Transactional
    public void selectSuggestion(Long orderId, Long suggestionId) {
        log.info("Customer with order [{}] Selecting suggestion [{}]", orderId, suggestionId);
        Suggestion suggestion = suggestionService.findById(suggestionId);
        Order order = orderService.findById(orderId);

        if (!order.getSuggestions().contains(suggestion)) {
            log.error("Suggestion isn't for this order throwing exception");
            throw new NotFoundException("Couldn't find that suggestion in your orders");
        }
        switch (order.getOrderStatus()) {
            case AWAITING_TECHNICIAN_ARRIVAL, STARTED, FINISHED, PAID -> {
                log.error("Invalid order status throwing exception");
                throw new CustomException("You can't select suggestions anymore");
            }
        }

        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_ARRIVAL);
        orderService.save(order);
    }


    @Override
    @Transactional
    public void sendSuggestion(Long technicianId, SendSuggestionDto sendSuggestionDto) {
        log.info("Technician with id [{}] is trying to send a new suggestion [{}] for this order [{}]"
                , technicianId, sendSuggestionDto, sendSuggestionDto.getOrderID());

        Set<ConstraintViolation<SendSuggestionDto>> violations = validator.validate(sendSuggestionDto);
        if (violations.isEmpty()) {
            log.info("Information is validated - commencing registration");
            Order order = orderService.findById(sendSuggestionDto.getOrderID());
            SubServices subServices = subServicesService.findById(order.getSubServices().getId());
            Technician technician = technicianService.findById(technicianId);

            checkCondition(technician, sendSuggestionDto, subServices, order);
            Suggestion suggestion = mapDtoValues(technician, sendSuggestionDto);

            try {
                log.info("Connecting to [{}]", suggestionService);
                order.getSuggestions().add(suggestion);
                order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SELECTION);
                orderService.save(order);
                suggestion.setOrder(order);
                suggestionService.save(suggestion);
                return;
            } catch (PersistenceException e) {
                log.error("PersistenceException occurred throwing CustomException ... ");
                throw new CustomException(e.getMessage());
            }
        }
        String violationMessages = getViolationMessages(violations);
        throw new CustomException(violationMessages);
    }

    @Override
    public List<Suggestion> getSuggestionByTechnicianPoint(Long orderId, boolean ascending) {
        log.info("Getting suggestions ordered by technicianPoints for order [{}]", orderId);
        Order order = orderService.findById(orderId);
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
        log.info("Getting suggestions ordered by price for order [{}]", orderId);
        Order order = orderService.findById(orderId);
        List<Suggestion> suggestions = order.getSuggestions();
        Comparator<Suggestion> priceComparing = Comparator.comparing(Suggestion::getSuggestedPrice);
        if (!ascending) {
            priceComparing = priceComparing.reversed();
        }
        suggestions.sort(priceComparing);
        return suggestions;
    }

    protected void checkCondition(Technician technician, SendSuggestionDto sendSuggestionDto, SubServices subServices, Order order) {
        log.info("Checking suggestion conditions");
        switch (order.getOrderStatus()) {
            case AWAITING_TECHNICIAN_ARRIVAL, STARTED, FINISHED, PAID -> {
                log.error("Invalid order status throwing exception");
                throw new CustomException("You can't send a suggestion for this order");
            }
        }
        List<Technician> technicians = subServices.getTechnicians();
        if (!technicians.contains(technician)) {
            log.error("Technician doesn't have this service throwing exception");
            throw new CustomException("You don't have this sub service");
        }
        if (sendSuggestionDto.getSuggestedPrice() < subServices.getBaseWage()) {
            log.error("SuggestedPrice is lower than base wage throwing exception");
            throw new CustomException("Price can't be lower than base wage");
        }
        LocalDateTime localDateTime = toLocalDateTime(sendSuggestionDto.getSuggestedTime(), sendSuggestionDto.getSuggestedDate());
        LocalDateTime now = LocalDateTime.now();
        if (localDateTime.isBefore(now)) {
            log.error("Date is before now throwing exception");
            throw new CustomException("Date and time can't be before now");
        }
    }

    protected String getViolationMessages(Set<ConstraintViolation<SendSuggestionDto>> violations) {
        log.error("SendSuggestionDto violates some fields throwing exception");
        StringBuilder messageBuilder = new StringBuilder();
        for (ConstraintViolation<SendSuggestionDto> violation : violations) {
            messageBuilder.append("\n").append(violation.getMessage());
        }
        return messageBuilder.toString().trim();
    }

    protected LocalDateTime toLocalDateTime(String time, String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);

        return localDate.atTime(localTime);
    }

    protected LocalTime convertTime(String duration) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(duration, timeFormatter);
    }

    protected Suggestion mapDtoValues(Technician technician, SendSuggestionDto sendSuggestionDto) {
        log.info("Mapping Dto values [{}]", sendSuggestionDto);
        Suggestion suggestion = new Suggestion();
        suggestion.setDate(LocalDateTime.now());
        suggestion.setSuggestedPrice(sendSuggestionDto.getSuggestedPrice());
        suggestion.setTechnician(technician);

        LocalDateTime localDateTime = toLocalDateTime(sendSuggestionDto.getSuggestedTime(), sendSuggestionDto.getSuggestedDate());
        suggestion.setSuggestedDate(localDateTime);

        suggestion.setDuration(convertTime(sendSuggestionDto.getDuration()));
        return suggestion;
    }
}
