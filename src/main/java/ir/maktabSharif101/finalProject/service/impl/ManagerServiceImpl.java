package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.entity.Manager;
import ir.maktabSharif101.finalProject.repository.ManagerRepository;
import ir.maktabSharif101.finalProject.service.ManagerService;
import ir.maktabSharif101.finalProject.service.base.BaseUserServiceImpl;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;
import ir.maktabSharif101.finalProject.utils.CustomException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import javax.persistence.PersistenceException;
import java.util.Set;

public class ManagerServiceImpl extends BaseUserServiceImpl<Manager, ManagerRepository>
        implements ManagerService {

    private final Validator validator;

    public ManagerServiceImpl(ManagerRepository baseRepository, Validator validator) {
        super(baseRepository);
        this.validator = validator;
    }

    @Override
    public Manager register(RegisterDto registerDto) {
        Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
        if (violations.isEmpty()) {
            checkCondition(registerDto);
            Manager manager = mapDtoValues(registerDto);
            try {
                return baseRepository.save(manager);
            } catch (PersistenceException e) {
                System.out.println(e.getMessage());
            }
        }
        String violationMessages = getViolationMessages(violations);
        throw new CustomException("ValidationException", violationMessages);
    }

    private String getViolationMessages(Set<ConstraintViolation<RegisterDto>> violations) {
        StringBuilder messageBuilder = new StringBuilder();
        for (ConstraintViolation<RegisterDto> violation : violations) {
            messageBuilder.append("\n").append(violation.getMessage());
        }
        return messageBuilder.toString().trim();
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
