package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.entity.Manager;
import ir.maktabSharif101.finalProject.repository.ManagerRepository;
import ir.maktabSharif101.finalProject.service.ManagerService;
import ir.maktabSharif101.finalProject.service.base.BaseUserServiceImpl;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;
import ir.maktabSharif101.finalProject.utils.CustomException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PersistenceException;
import java.util.Set;

@Slf4j
public class ManagerServiceImpl extends BaseUserServiceImpl<Manager, ManagerRepository>
        implements ManagerService {

    private final Validator validator;

    public ManagerServiceImpl(ManagerRepository baseRepository, Validator validator) {
        super(baseRepository);
        this.validator = validator;
    }

    @Override
    public Manager register(RegisterDto registerDto) {
        log.info("Registering with this data [{}]", registerDto);
        Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
        if (violations.isEmpty()) {
            log.info("Information's are validated - commencing registration");
            checkCondition(registerDto);
            Manager manager = mapDtoValues(registerDto);
            try {
                log.info("Connecting to [{}]",baseRepository);
                return baseRepository.save(manager);
            } catch (PersistenceException e) {
                System.out.println(e.getMessage());
            }
        }
        String violationMessages = getViolationMessages(violations);
        throw new CustomException("ValidationException", violationMessages);
    }

    private String getViolationMessages(Set<ConstraintViolation<RegisterDto>> violations) {
        log.error("RegisterDto violates some fields throwing exception");
        StringBuilder messageBuilder = new StringBuilder();
        for (ConstraintViolation<RegisterDto> violation : violations) {
            messageBuilder.append("\n").append(violation.getMessage());
        }
        return messageBuilder.toString().trim();
    }

    protected void checkCondition(RegisterDto registerDto) {
        log.info("Checking registration conditions");
        if (existsByEmailAddress(registerDto.getEmailAddress())) {
            log.error("{} already exists in the database throwing exception", registerDto.getEmailAddress());
            throw new CustomException("DuplicateEmailAddress", "Email address already exists in the database");
        }
    }

    protected Manager mapDtoValues(RegisterDto registerDto) {
        log.info("Mapping [{}] values",registerDto);
        Manager manager = new Manager();
        manager.setFirstname(registerDto.getFirstname());
        manager.setLastname(registerDto.getLastname());
        manager.setEmailAddress(registerDto.getEmailAddress());
        manager.setPassword(registerDto.getPassword());
        return manager;
    }
}
