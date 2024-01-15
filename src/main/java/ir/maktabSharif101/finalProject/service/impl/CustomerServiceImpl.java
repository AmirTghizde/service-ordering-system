package ir.maktabSharif101.finalProject.service.impl;


import ir.maktabSharif101.finalProject.entity.Customer;
import ir.maktabSharif101.finalProject.repository.CustomerRepository;
import ir.maktabSharif101.finalProject.service.CustomerService;
import ir.maktabSharif101.finalProject.service.base.BaseUserServiceImpl;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;
import ir.maktabSharif101.finalProject.utils.CustomException;
import ir.maktabSharif101.finalProject.utils.Validation;

import javax.persistence.PersistenceException;


public class CustomerServiceImpl extends BaseUserServiceImpl<Customer, CustomerRepository> implements CustomerService {
    public CustomerServiceImpl(CustomerRepository baseRepository) {
        super(baseRepository);
    }

    @Override
    public Customer register(RegisterDto registerDto) {
        validateInfo(registerDto);
        checkCondition(registerDto);
        Customer customer= mapDtoValues(registerDto);
        
        try {
            return baseRepository.save(customer);
        }catch (PersistenceException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return customer;
    }

    private void validateInfo(RegisterDto registerDto) {
        if (!Validation.isValidName(registerDto.getFirstname())|| !Validation.isValidName(registerDto.getLastname())){
            throw new CustomException("InvalidName","Names must only contain letters");
        } else if (!Validation.isValidEmail(registerDto.getEmailAddress())) {
            throw new CustomException("InvalidEmail","Check the email address it is wrong");
        }else if (!Validation.isValidPassword(registerDto.getPassword())) {
            throw new CustomException("InvalidPassword","Passwords must be a combination of letters and numbers");
        }
    }

    protected void checkCondition(RegisterDto registerDto) {
        if (existsByEmailAddress(registerDto.getEmailAddress())){
            throw new CustomException("DuplicateEmailAddress","Email address already exists in the database");
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
