package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.repository.base.BaseUserRepository;
import com.Maktab101.SpringProject.service.CustomerService;
import com.Maktab101.SpringProject.service.base.BaseUserServiceImpl;
import com.Maktab101.SpringProject.service.dto.RegisterDto;
import com.Maktab101.SpringProject.utils.CustomException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Set;

@Slf4j
@Service
public class CustomerServiceImpl extends BaseUserServiceImpl<Customer> implements CustomerService {

    private final Validator validator;

    public CustomerServiceImpl(BaseUserRepository<Customer> baseRepository, Validator validator) {
        super(baseRepository);
        this.validator = validator;
    }


    @Override
    public Customer register(RegisterDto registerDto) {
        log.info("Registering with this data [{}]", registerDto);
        Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
        if (violations.isEmpty()) {
            log.info("Information is validated - commencing registration");
            checkCondition(registerDto);
            Customer customer = mapDtoValues(registerDto);
            try {
                log.info("Connecting to [{}]",baseRepository);
                return baseRepository.save(customer);
            } catch (PersistenceException e) {
                log.error("PersistenceException occurred printing ... ");
                System.out.println(e.getMessage());
            }
        }
        String violationMessages = getViolationMessages(violations);
        throw new CustomException("ValidationException", violationMessages);
    }

    private String getViolationMessages(Set<ConstraintViolation<RegisterDto>> violations) {
        log.error("RegisterDto violates some fields throwing exception");
        StringBuilder messageBuilder = new StringBuilder();
        for (ConstraintViolation<RegisterDto> violation : violations) {
            messageBuilder.append("\n").append(violation.getMessage());
        }
        return messageBuilder.toString().trim();
    }

    protected void checkCondition(RegisterDto registerDto) {
        log.info("Checking registration conditions");
        if (existsByEmailAddress(registerDto.getEmailAddress())) {
            log.error("[{}] already exists in the database throwing exception", registerDto.getEmailAddress());
            throw new CustomException("DuplicateEmailAddress", "Email address already exists in the database");
        }
    }

    protected Customer mapDtoValues(RegisterDto registerDto) {
        log.info("Mapping [{}] values",registerDto);
        Customer customer = new Customer();
        customer.setFirstname(registerDto.getFirstname());
        customer.setLastname(registerDto.getLastname());
        customer.setEmail(registerDto.getEmailAddress());
        customer.setPassword(registerDto.getPassword());
        return customer;
    }

}
