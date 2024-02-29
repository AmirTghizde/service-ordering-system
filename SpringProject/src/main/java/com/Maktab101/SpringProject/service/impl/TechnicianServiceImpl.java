package com.Maktab101.SpringProject.service.impl;


import com.Maktab101.SpringProject.dto.users.RequestDto;
import com.Maktab101.SpringProject.dto.users.SearchRequestDto;
import com.Maktab101.SpringProject.model.EmailVerification;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.model.enums.*;
import com.Maktab101.SpringProject.repository.TechnicianRepository;
import com.Maktab101.SpringProject.service.EmailVerificationService;
import com.Maktab101.SpringProject.service.FilterSpecification;
import com.Maktab101.SpringProject.service.TechnicianService;
import com.Maktab101.SpringProject.service.base.BaseUserServiceImpl;
import com.Maktab101.SpringProject.dto.users.RegisterDto;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import com.Maktab101.SpringProject.utils.exceptions.DuplicateValueException;
import com.Maktab101.SpringProject.utils.exceptions.NotFoundException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class TechnicianServiceImpl extends BaseUserServiceImpl<Technician> implements TechnicianService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final FilterSpecification<Technician> filterSpecification;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public TechnicianServiceImpl(TechnicianRepository baseRepository, BCryptPasswordEncoder passwordEncoder, FilterSpecification<Technician> filterSpecification, EmailVerificationService emailVerificationService) {
        super(baseRepository, passwordEncoder);
        this.passwordEncoder = passwordEncoder;
        this.filterSpecification = filterSpecification;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    public Technician register(RegisterDto registerDto) {
        log.info("Registering with this data [{}]", registerDto);

        checkCondition(registerDto);
        Technician technician = mapDtoValues(registerDto);
        Technician savedTechnician = save(technician);

        // Send the registration email
        EmailVerification emailVerification = emailVerificationService.generateToken(savedTechnician);
        emailVerificationService.sendEmail(emailVerification);
        return savedTechnician;
    }

    @Override
    public void verify(Long technicianId, String token) {
        Technician technician = findById(technicianId);
        technician.setStatus(TechnicianStatus.AWAITING_CONFIRMATION);
        try {
            save(technician);
            emailVerificationService.deleteByToken(token);
        } catch (PersistenceException e) {
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public List<Technician> findAll() {
        return baseRepository.findAll();
    }

    @Override
    public void confirmTechnician(Long technicianId) {
        log.info("Confirming technician");
        Technician technician = findById(technicianId);

        technician.setStatus(TechnicianStatus.CONFIRMED);
        technician.setIsEnabled(true);
        try {
            log.info("Connecting to [{}]", baseRepository);
            baseRepository.save(technician);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public void saveImage(Long technicianId, byte[] imageData) {

        Technician technician = findById(technicianId);

        log.info("Adding image to [{}] profile", technician.getEmail());

        technician.setImageData(imageData);
        try {
            baseRepository.save(technician);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public List<Technician> handelFiltering(RequestDto requestDto) {
        Specification<Technician> specificationList = filterSpecification.getSpecificationList(
                requestDto.getSearchRequestDto(),
                requestDto.getGlobalOperator());

        return baseRepository.findAll(specificationList);
    }

    @Override
    public void addCredit(Long technicianId, double amount) {
        double seventyPercent = amount * 0.7;
        Technician technician = findById(technicianId);
        double balance = technician.getBalance();
        balance += seventyPercent;
        technician.setBalance(balance);
        try {
            baseRepository.save(technician);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public void addPoints(Long technicianId, double amount) {
        Technician technician = findById(technicianId);
        double score = technician.getScore();
        score = (score + amount) / 2;

        technician.setScore(score);
        technician.setOrdersFinished(technician.getOrdersFinished() + 1);
        try {
            baseRepository.save(technician);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public void reducePoints(Long technicianId, double amount) {
        log.info("Reducing points [technician:{}] points by [{}]", technicianId, amount);
        Technician technician = findById(technicianId);
        double score = technician.getScore();
        score -= amount;
        log.info("The new score is [{}]", score);

        if (score < 0) {
            log.info("Score is lower than 0 disabling account");
            technician.setIsEnabled(false);
        }

        technician.setScore(score);
        try {
            baseRepository.save(technician);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public RequestDto getRequestDto(Long technicianId, OrderStatus status) {
        // Find technician's orders
        SearchRequestDto searchRequest1 = SearchRequestDto.builder()
                .column("id")
                .value(technicianId.toString())
                .operation(Operation.DOUBLE_JOIN)
                .joinTable("SelectedSuggestion,technician")
                .build();

        // Find the ones matching the status
        SearchRequestDto searchRequest2 = SearchRequestDto.builder()
                .column("orderStatus")
                .value(String.valueOf(status))
                .operation(Operation.EQUAL)
                .joinTable("")
                .build();

        // Build the dto
        return RequestDto.builder()
                .searchRequestDto(List.of(searchRequest1, searchRequest2))
                .globalOperator(GlobalOperator.AND)
                .build();
    }

    protected byte[] imageToBytes(String imageAddress) {
        log.info("Converting image to bytes");
        try {
            File imageFile = new File(imageAddress);
            return Files.readAllBytes(imageFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void checkCondition(RegisterDto registerDto) {
        log.info("Checking registration conditions");
        if (existsByEmailAddress(registerDto.getEmailAddress())) {
            log.error("[{}] already exists in the database throwing exception", registerDto.getEmailAddress());
            throw new DuplicateValueException("This email is already being used in the database: " + registerDto.getEmailAddress());
        }
    }

    protected Technician mapDtoValues(RegisterDto registerDto) {
        log.info("Mapping [{}] values", registerDto);
        Technician technician = new Technician();
        technician.setFirstname(registerDto.getFirstname());
        technician.setLastname(registerDto.getLastname());
        technician.setEmail(registerDto.getEmailAddress());
        technician.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        technician.setScore(0);
        technician.setBalance(0);
        technician.setOrdersFinished(0L);
        technician.setSubServices(new ArrayList<>());
        technician.setStatus(TechnicianStatus.NEW);
        technician.setIsEnabled(false);
        technician.setRole(Role.ROLE_TECHNICIAN);

        String defaultPath = "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\deafualt.jpg";
        byte[] image = imageToBytes(defaultPath);
        technician.setImageData(image);

        return technician;
    }
}
