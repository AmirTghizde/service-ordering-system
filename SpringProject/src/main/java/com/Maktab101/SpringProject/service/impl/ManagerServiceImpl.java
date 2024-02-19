package com.Maktab101.SpringProject.service.impl;


import com.Maktab101.SpringProject.model.Manager;
import com.Maktab101.SpringProject.repository.ManagerRepository;
import com.Maktab101.SpringProject.service.ManagerService;
import com.Maktab101.SpringProject.service.base.BaseUserServiceImpl;
import com.Maktab101.SpringProject.dto.users.RegisterDto;
import com.Maktab101.SpringProject.utils.HashUtils;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import com.Maktab101.SpringProject.utils.exceptions.DuplicateValueException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;

@Slf4j
@Service
public class ManagerServiceImpl extends BaseUserServiceImpl<Manager>
        implements ManagerService {



    public ManagerServiceImpl(ManagerRepository baseRepository) {
        super(baseRepository);
    }

    @Override
    public Manager register(RegisterDto registerDto) {
        log.info("Registering with this data [{}]", registerDto);
            log.info("Information is validated - commencing registration");
            checkCondition(registerDto);
            Manager manager = mapDtoValues(registerDto);
            try {
                log.info("Connecting to [{}]", baseRepository);
                return baseRepository.save(manager);
            } catch (PersistenceException e) {
                log.error("PersistenceException occurred throwing CustomException ... ");
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
        manager.setPassword(HashUtils.hash(registerDto.getPassword()));

        int number = random.nextInt(90000) + 10000;
        manager.setManagerCode("M" + number);

        return manager;
    }
}
