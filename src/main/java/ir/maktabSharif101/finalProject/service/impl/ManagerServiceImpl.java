package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.entity.Customer;
import ir.maktabSharif101.finalProject.entity.Manager;
import ir.maktabSharif101.finalProject.repository.ManagerRepository;
import ir.maktabSharif101.finalProject.service.ManagerService;
import ir.maktabSharif101.finalProject.service.base.BaseUserServiceImpl;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;
import ir.maktabSharif101.finalProject.utils.CustomException;
import ir.maktabSharif101.finalProject.utils.Validation;

import javax.persistence.PersistenceException;

public class ManagerServiceImpl extends BaseUserServiceImpl<Manager, ManagerRepository>
implements ManagerService {
    public ManagerServiceImpl(ManagerRepository baseRepository) {
        super(baseRepository);
    }

    @Override
    public Manager register(RegisterDto registerDto) {
        validateInfo(registerDto);
        checkCondition(registerDto);
        Manager manager= mapDtoValues(registerDto);

        try {
            return baseRepository.save(manager);
        }catch (PersistenceException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return manager;
    }
    private void validateInfo(RegisterDto registerDto) {
        if (!Validation.isValidName(registerDto.getFirstname())|| !Validation.isValidName(registerDto.getLastname())){
            throw new CustomException("4422","Invalid usage of numbers in name");
        } else if (!Validation.isValidEmail(registerDto.getEmailAddress())) {
            throw new CustomException("4422","Invalid email");
        }else if (!Validation.isValidPassword(registerDto.getPassword())) {
            throw new CustomException("4422","Passwords must contain: \nAt least 1 number\nAt least 1 big letter" +
                    "\nAt least 1 small letter\nAt least one of these symbols  # $ % &\nAnd at least 8 characters");
        }
    }

    protected void checkCondition(RegisterDto registerDto) {
        if (existsByEmailAddress(registerDto.getEmailAddress())){
            throw new CustomException("4499","Duplicate email address");
        }
    }

    protected Manager mapDtoValues(RegisterDto registerDto) {
        Manager manager = new Manager();
        manager.setFirstname(registerDto.getFirstname());
        manager.setLastname(registerDto.getLastname());
        manager.setEmailAddress(registerDto.getEmailAddress());
        manager.setPassword(registerDto.getPassword());
        return manager;
    }
}
