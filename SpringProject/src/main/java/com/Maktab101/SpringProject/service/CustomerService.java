package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.service.base.BaseUserService;
import com.Maktab101.SpringProject.dto.users.RegisterDto;

import java.util.List;

public interface CustomerService extends BaseUserService<Customer> {
    Customer register (RegisterDto registerDto);

    List<Customer> sort(List<String> sortingFields);
}
