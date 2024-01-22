package com.Maktab101.SpringProject.service.impl;


import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.model.SubServices;
import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.repository.SuggestionRepository;
import com.Maktab101.SpringProject.service.OrderService;
import com.Maktab101.SpringProject.service.SubServicesService;
import com.Maktab101.SpringProject.service.SuggestionService;
import com.Maktab101.SpringProject.service.TechnicianService;
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
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SuggestionServiceImpl implements SuggestionService {
    private final SuggestionRepository suggestionRepository;
    private final OrderService orderService;
    private final SubServicesService subServicesService;
    private final TechnicianService technicianService;
    private final Validator validator;


    @Autowired
    public SuggestionServiceImpl(SuggestionRepository suggestionRepository, OrderService orderService,
                                 SubServicesService subServicesService, TechnicianService technicianService,
                                 Validator validator) {
        this.suggestionRepository = suggestionRepository;
        this.orderService = orderService;
        this.subServicesService = subServicesService;
        this.technicianService = technicianService;
        this.validator = validator;
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


            checkCondition(technician,suggestionDto,subServices);
            Suggestion suggestion = mapDtoValues(technician,suggestionDto);

            try {
                log.info("Connecting to [{}]", suggestionRepository);
                order.getSuggestions().add(suggestion);
                order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SELECTION);
                orderService.save(order);
                suggestion.setOrder(order);
                suggestionRepository.save(suggestion);
                return;
            } catch (PersistenceException e) {
                System.out.println(e.getMessage());
            }
        }
        String violationMessages = getViolationMessages(violations);
        throw new CustomException("ValidationException", violationMessages);
    }

    @Override
    public Suggestion findById(Long suggestionId) {
        return suggestionRepository.findById(suggestionId).orElseThrow(
                ()->new CustomException("SuggestionNotFound","We can't find that suggestion")
        );
    }

    protected void checkCondition(Technician technician,SuggestionDto suggestionDto, SubServices subServices) {
        log.info("Checking suggestion conditions");
        List<Technician> technicians = subServices.getTechnicians();
        if (!technicians.contains(technician)){
            throw new CustomException("WrongSubService", "You don't have this sub service");
        }
        if (suggestionDto.getSuggestedPrice() < subServices.getBaseWage()) {
            log.error("SuggestedPrice is lower than base wage throwing exception");
            throw new CustomException("InvalidPrice", "Price can't be lower than base wage");
        }
        LocalDateTime localDateTime = convertDateAndTime(suggestionDto.getSuggestedTime(), suggestionDto.getSuggestedDate());
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

    private LocalDateTime convertDateAndTime(String time, String date) {
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
    private Suggestion mapDtoValues(Technician technician,SuggestionDto suggestionDto) {
        Suggestion suggestion = new Suggestion();
        suggestion.setDate(LocalDateTime.now());
        suggestion.setSuggestedPrice(suggestionDto.getSuggestedPrice());
        suggestion.setTechnician(technician);

        LocalDateTime localDateTime = convertDateAndTime(suggestionDto.getSuggestedTime(), suggestionDto.getSuggestedDate());
        suggestion.setSuggestedDate(localDateTime);

        suggestion.setDuration(convertTime(suggestionDto.getDuration()));
        return suggestion;
    }


}
