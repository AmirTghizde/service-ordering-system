package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.model.SubServices;
import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.service.*;
import com.Maktab101.SpringProject.service.dto.SuggestionDto;
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
    public void selectSugestion(Long orderId, Long suggestionId) {
        Suggestion suggestion = suggestionService.findById(suggestionId);
        Order order = orderService.findById(orderId);

        if (!order.getSuggestions().contains(suggestion)) {
            throw new CustomException("InvalidSuggestion", "We can't find that suggestion in your order");
        }

        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_ARRIVAL);
        orderService.save(order);
    }


    @Override
    @Transactional
    public void sendSuggestion(Long technicianId, SuggestionDto suggestionDto) {
        log.info("Technician with id [{}] is trying to send a new suggestion [{}] for this order [{}]"
                , technicianId, suggestionDto, suggestionDto.getOrderID());

        Set<ConstraintViolation<SuggestionDto>> violations = validator.validate(suggestionDto);
        if (violations.isEmpty()) {
            log.info("Information is validated - commencing registration");
            Order order = orderService.findById(suggestionDto.getOrderID());
            SubServices subServices = subServicesService.findById(order.getSubServices().getId());
            Technician technician = technicianService.findById(technicianId);

            checkCondition(technician, suggestionDto, subServices, order);
            Suggestion suggestion = mapDtoValues(technician, suggestionDto);

            try {
                log.info("Connecting to [{}]", suggestionService);
                order.getSuggestions().add(suggestion);
                order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SELECTION);
                orderService.save(order);
                suggestion.setOrder(order);
                suggestionService.save(suggestion);
                return;
            } catch (PersistenceException e) {
                System.out.println(e.getMessage());
            }
        }
        String violationMessages = getViolationMessages(violations);
        throw new CustomException("ValidationException", violationMessages);
    }

    @Override
    public List<Suggestion> getSuggestionByTechnicianPoint(Long orderId, boolean ascending) {
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
        Order order = orderService.findById(orderId);
        List<Suggestion> suggestions = order.getSuggestions();
        Comparator<Suggestion> priceComparing = Comparator.comparing(Suggestion::getSuggestedPrice);
        if (!ascending) {
            priceComparing = priceComparing.reversed();
        }
        suggestions.sort(priceComparing);
        return suggestions;
    }

    protected void checkCondition(Technician technician, SuggestionDto suggestionDto, SubServices subServices, Order order) {
        log.info("Checking suggestion conditions");
        switch (order.getOrderStatus()) {
            case AWAITING_TECHNICIAN_ARRIVAL, STARTED, FINISHED, PAID ->
                    throw new CustomException("InvalidAction", "You can't send a suggestion for this order");
        }
        List<Technician> technicians = subServices.getTechnicians();
        if (!technicians.contains(technician)) {
            throw new CustomException("WrongSubService", "You don't have this sub service");
        }
        if (suggestionDto.getSuggestedPrice() < subServices.getBaseWage()) {
            log.error("SuggestedPrice is lower than base wage throwing exception");
            throw new CustomException("InvalidPrice", "Price can't be lower than base wage");
        }
        LocalDateTime localDateTime = toLocalDateTime(suggestionDto.getSuggestedTime(), suggestionDto.getSuggestedDate());
        LocalDateTime now = LocalDateTime.now();
        if (localDateTime.isBefore(now)) {
            log.error("Date is before now throwing exception");
            throw new CustomException("InvalidDateAndTime", "Date and time can't be before now");
        }
    }

    private String getViolationMessages(Set<ConstraintViolation<SuggestionDto>> violations) {
        log.error("SuggestionDto violates some fields throwing exception");
        StringBuilder messageBuilder = new StringBuilder();
        for (ConstraintViolation<SuggestionDto> violation : violations) {
            messageBuilder.append("\n").append(violation.getMessage());
        }
        return messageBuilder.toString().trim();
    }

    private LocalDateTime toLocalDateTime(String time, String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);

        return localDate.atTime(localTime);
    }

    private LocalTime convertTime(String duration) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(duration, timeFormatter);
    }

    private Suggestion mapDtoValues(Technician technician, SuggestionDto suggestionDto) {
        Suggestion suggestion = new Suggestion();
        suggestion.setDate(LocalDateTime.now());
        suggestion.setSuggestedPrice(suggestionDto.getSuggestedPrice());
        suggestion.setTechnician(technician);

        LocalDateTime localDateTime = toLocalDateTime(suggestionDto.getSuggestedTime(), suggestionDto.getSuggestedDate());
        suggestion.setSuggestedDate(localDateTime);

        suggestion.setDuration(convertTime(suggestionDto.getDuration()));
        return suggestion;
    }
}
