package ir.maktabSharif101.finalProject.service.impl;


import ir.maktabSharif101.finalProject.entity.Customer;
import ir.maktabSharif101.finalProject.repository.CustomerRepository;
import ir.maktabSharif101.finalProject.service.CustomerService;
import ir.maktabSharif101.finalProject.service.base.BaseUserServiceImpl;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;
import ir.maktabSharif101.finalProject.utils.CustomException;

import javax.persistence.PersistenceException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;


public class CustomerServiceImpl extends BaseUserServiceImpl<Customer, CustomerRepository> implements CustomerService {

    private final Validator validator;

    public CustomerServiceImpl(CustomerRepository baseRepository, Validator validator) {
        super(baseRepository);
        this.validator = validator;
    }

    @Override
    public Customer register(RegisterDto registerDto) {
        Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
        if (violations.isEmpty()) {
        checkCondition(registerDto);
        Customer customer = mapDtoValues(registerDto);
            try {
                return baseRepository.save(customer);
            } catch (PersistenceException e) {
                System.out.println(e.getMessage());
            }
        }
        String violationMessages = getViolationMessages(violations);
        throw new CustomException("ValidationException", violationMessages);
    }
    private String getViolationMessages(Set<ConstraintViolation<RegisterDto>> violations) {
        StringBuilder messageBuilder = new StringBuilder();
        for (ConstraintViolation<RegisterDto> violation : violations) {
            messageBuilder.append("\n").append(violation.getMessage());
        }
        return messageBuilder.toString().trim();
    }

    protected void checkCondition(RegisterDto registerDto) {
        if (existsByEmailAddress(registerDto.getEmailAddress())) {
            throw new CustomException("DuplicateEmailAddress", "Email address already exists in the database");
        }
    }
    protected Customer mapDtoValues(RegisterDto registerDto) {
        Customer customer = new Customer();
        customer.setFirstname(registerDto.getFirstname());
        customer.setLastname(registerDto.getLastname());
        customer.setEmailAddress(registerDto.getEmailAddress());
        customer.setPassword(registerDto.getPassword());
        return customer;
    }

}
