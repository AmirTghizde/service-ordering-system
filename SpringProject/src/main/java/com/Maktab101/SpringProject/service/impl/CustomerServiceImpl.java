package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.model.enums.Role;
import com.Maktab101.SpringProject.repository.base.BaseUserRepository;
import com.Maktab101.SpringProject.service.CustomerService;
import com.Maktab101.SpringProject.service.base.BaseUserServiceImpl;
import com.Maktab101.SpringProject.dto.users.RegisterDto;
import com.Maktab101.SpringProject.utils.HashUtils;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import com.Maktab101.SpringProject.utils.exceptions.DuplicateValueException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl extends BaseUserServiceImpl<Customer>
        implements CustomerService {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(BaseUserRepository<Customer> baseRepository, BCryptPasswordEncoder passwordEncoder) {
        super(baseRepository);
        this.passwordEncoder = passwordEncoder;
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
    public List<Customer> filter(Specification<Customer> specification) {
        return baseRepository.findAll(specification);
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
    public List<Order> getOrderHistory(Long customerId) {
        Customer customer = findById(customerId);

        return customer.getOrders().stream()
                .filter(order -> order.getOrderStatus().equals(OrderStatus.FINISHED) ||
                        order.getOrderStatus().equals(OrderStatus.PAID))
                .collect(Collectors.toList());

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
