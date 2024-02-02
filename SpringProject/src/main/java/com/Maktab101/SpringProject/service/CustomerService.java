package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.service.base.BaseUserService;
import com.Maktab101.SpringProject.dto.RegisterDto;

public interface CustomerService extends BaseUserService<Customer> {
    Customer register (RegisterDto registerDto);
}
