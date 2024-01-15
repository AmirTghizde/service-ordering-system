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
        Manager manager = mapDtoValues(registerDto);

        try {
            return baseRepository.save(manager);
        } catch (PersistenceException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return manager;
    }

    private void validateInfo(RegisterDto registerDto) {
        if (!Validation.isValidName(registerDto.getFirstname()) || !Validation.isValidName(registerDto.getLastname())) {
            throw new CustomException("InvalidName", "Names must only contain letters");
        } else if (!Validation.isValidEmail(registerDto.getEmailAddress())) {
            throw new CustomException("InvalidEmail", "Check the email address it is wrong");
        } else if (!Validation.isValidPassword(registerDto.getPassword())) {
            throw new CustomException("InvalidPassword", "Passwords must be a combination of letters and numbers");
        }
    }

    protected void checkCondition(RegisterDto registerDto) {
        if (existsByEmailAddress(registerDto.getEmailAddress())) {
            throw new CustomException("DuplicateEmailAddress", "Email address already exists in the database");
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
