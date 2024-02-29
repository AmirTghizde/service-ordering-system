package com.Maktab101.SpringProject.service.impl;


import com.Maktab101.SpringProject.model.EmailVerification;
import com.Maktab101.SpringProject.model.Manager;
import com.Maktab101.SpringProject.model.enums.Role;
import com.Maktab101.SpringProject.repository.ManagerRepository;
import com.Maktab101.SpringProject.service.EmailVerificationService;
import com.Maktab101.SpringProject.service.ManagerService;
import com.Maktab101.SpringProject.service.base.BaseUserServiceImpl;
import com.Maktab101.SpringProject.dto.users.RegisterDto;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import com.Maktab101.SpringProject.utils.exceptions.DuplicateValueException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class ManagerServiceImpl extends BaseUserServiceImpl<Manager>
        implements ManagerService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    public ManagerServiceImpl(ManagerRepository baseRepository, BCryptPasswordEncoder passwordEncoder, EmailVerificationService emailVerificationService) {
        super(baseRepository, passwordEncoder);
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    public Manager register(RegisterDto registerDto) {
        log.info("Registering with this data [{}]", registerDto);
        log.info("Information is validated - commencing registration");
        checkCondition(registerDto);
        Manager manager = mapDtoValues(registerDto);
        Manager savedManager = save(manager);

        // Send the registration email
        EmailVerification emailVerification = emailVerificationService.generateToken(savedManager);
        emailVerificationService.sendEmail(emailVerification);
        return savedManager;
    }

    @Override
    public void verify(Long managerId, String token) {
        Manager manager = findById(managerId);
        manager.setIsEnabled(true);
        try {
            save(manager);
            emailVerificationService.deleteByToken(token);
        }catch (PersistenceException e){
            throw new CustomException(e.getMessage());
        }
    }

    protected void checkCondition(RegisterDto registerDto) {
        log.info("Checking registration conditions");
        if (existsByEmailAddress(registerDto.getEmailAddress())) {
            log.error("[{}] already exists in the database throwing exception", registerDto.getEmailAddress());
            throw new DuplicateValueException("This email is already being used in database: " + registerDto.getEmailAddress());
        }
    }

    protected Manager mapDtoValues(RegisterDto registerDto) {
        Random random = new Random();
        log.info("Mapping [{}] values", registerDto);
        Manager manager = new Manager();
        manager.setFirstname(registerDto.getFirstname());
        manager.setLastname(registerDto.getLastname());
        manager.setEmail(registerDto.getEmailAddress());
        manager.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        manager.setIsEnabled(false);
        manager.setRole(Role.ROLE_MANAGER);

        int number = random.nextInt(90000) + 10000;
        manager.setManagerCode("M" + number);

        return manager;
    }
}
