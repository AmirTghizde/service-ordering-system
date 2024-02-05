package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.service.base.BaseUserService;
import com.Maktab101.SpringProject.dto.users.RegisterDto;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CustomerService extends BaseUserService<Customer> {
    Customer register (RegisterDto registerDto);
    List<Customer> filter(Specification<Customer> specification);
    void payByCredit(Long customerId, double amount);
    void addCredit(Long customerId, double amount);
}
