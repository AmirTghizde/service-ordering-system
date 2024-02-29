package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.dto.users.RequestDto;
import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.service.base.BaseUserService;
import com.Maktab101.SpringProject.dto.users.RegisterDto;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CustomerService extends BaseUserService<Customer> {
    Customer register (RegisterDto registerDto);
    List<Customer> handelFiltering(RequestDto requestDto);
    void payByCredit(Long customerId, double amount);
    void addCredit(Long customerId, double amount);
    RequestDto getRequestDto(Long customerId, OrderStatus status);
    void verify(Long customerId,String token);
}
