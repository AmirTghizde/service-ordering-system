package ir.maktabSharif101.finalProject.service;


import ir.maktabSharif101.finalProject.entity.Customer;
import ir.maktabSharif101.finalProject.service.base.BaseUserService;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;

public interface CustomerService extends BaseUserService<Customer> {
    Customer register (RegisterDto registerDto);
}
