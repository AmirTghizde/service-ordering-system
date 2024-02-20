package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.dto.users.RequestDto;
import com.Maktab101.SpringProject.dto.users.SearchRequestDto;
import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.enums.GlobalOperator;
import com.Maktab101.SpringProject.model.enums.Operation;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.model.enums.Role;
import com.Maktab101.SpringProject.repository.base.BaseUserRepository;
import com.Maktab101.SpringProject.service.CustomerService;
import com.Maktab101.SpringProject.service.FilterSpecification;
import com.Maktab101.SpringProject.service.base.BaseUserServiceImpl;
import com.Maktab101.SpringProject.dto.users.RegisterDto;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import com.Maktab101.SpringProject.utils.exceptions.DuplicateValueException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl extends BaseUserServiceImpl<Customer>
        implements CustomerService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final FilterSpecification<Customer> filterSpecification;

    @Autowired
    public CustomerServiceImpl(BaseUserRepository<Customer> baseRepository, BCryptPasswordEncoder passwordEncoder, FilterSpecification<Customer> filterSpecification) {
        super(baseRepository);
        this.passwordEncoder = passwordEncoder;
        this.filterSpecification = filterSpecification;
    }

    @Override
    public Customer register(RegisterDto registerDto) {
        log.info("Registering with this data [{}]", registerDto);
        checkCondition(registerDto);
        Customer customer = mapDtoValues(registerDto);
        try {
            log.info("Connecting to [{}]", baseRepository);
            return baseRepository.save(customer);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public List<Customer> handelFiltering(RequestDto requestDto) {

        Specification<Customer> specificationList = filterSpecification.getSpecificationList(
                requestDto.getSearchRequestDto(),
                requestDto.getGlobalOperator());

        return baseRepository.findAll(specificationList);
    }

    @Override
    public void payByCredit(Long customerId, double amount) {
        Customer customer = findById(customerId);
        if (customer.getBalance() > amount) {
            double balance = customer.getBalance();
            balance -= amount;
            customer.setBalance(balance);
            baseRepository.save(customer);
        } else {
            throw new CustomException("Not enough credit use the online method");
        }
    }

    @Override
    public void addCredit(Long customerId, double amount) {
        Customer customer = findById(customerId);
        double balance = customer.getBalance();
        balance += amount;
        customer.setBalance(balance);
        baseRepository.save(customer);
    }

    @Override
    public RequestDto getRequestDto(Long customerId, OrderStatus status) {

        // Find customer's orders
        SearchRequestDto searchRequest1 = SearchRequestDto.builder()
                .column("id")
                .value(customerId.toString())
                .operation(Operation.JOIN)
                .joinTable("customer")
                .build();

        // Find the ones matching the status
        SearchRequestDto searchRequest2 = SearchRequestDto.builder()
                .column("orderStatus")
                .value(String.valueOf(status))
                .operation(Operation.EQUAL)
                .joinTable("")
                .build();

        // Build the dto
        return RequestDto.builder()
                .searchRequestDto(List.of(searchRequest1,searchRequest2))
                .globalOperator(GlobalOperator.AND)
                .build();
    }

    protected void checkCondition(RegisterDto registerDto) {
        log.info("Checking registration conditions");
        if (existsByEmailAddress(registerDto.getEmailAddress())) {
            log.error("[{}] already exists in the database throwing exception", registerDto.getEmailAddress());
            throw new DuplicateValueException("Email address already exists in the database");
        }
    }

    protected Customer mapDtoValues(RegisterDto registerDto) {
        log.info("Mapping [{}] values", registerDto);
        Customer customer = new Customer();
        customer.setFirstname(registerDto.getFirstname());
        customer.setLastname(registerDto.getLastname());
        customer.setEmail(registerDto.getEmailAddress());
        customer.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        customer.setIsEnabled(true);
        customer.setRole(Role.ROLE_CUSTOMER);
        return customer;
    }
}
