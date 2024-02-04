package com.Maktab101.SpringProject.service.impl;


import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.model.enums.TechnicianStatus;
import com.Maktab101.SpringProject.repository.TechnicianRepository;
import com.Maktab101.SpringProject.service.TechnicianService;
import com.Maktab101.SpringProject.service.base.BaseUserServiceImpl;
import com.Maktab101.SpringProject.dto.RegisterDto;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import com.Maktab101.SpringProject.utils.exceptions.DuplicateValueException;
import com.Maktab101.SpringProject.utils.exceptions.NotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class TechnicianServiceImpl extends BaseUserServiceImpl<Technician> implements TechnicianService {

    private final Validator validator;

    @Autowired
    public TechnicianServiceImpl(TechnicianRepository baseRepository, Validator validator) {
        super(baseRepository);
        this.validator = validator;
    }

    @Override
    public Technician register(RegisterDto registerDto) {
        log.info("Registering with this data [{}]", registerDto);
        Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
        if (violations.isEmpty()) {
            log.info("Information is validated - commencing registration");
            checkCondition(registerDto);
            Technician technician = mapDtoValues(registerDto);
            try {
                log.info("Connecting to [{}]", baseRepository);
                return baseRepository.save(technician);
            } catch (PersistenceException e) {
                log.error("PersistenceException occurred throwing CustomException ... ");
                throw new CustomException(e.getMessage());
            }
        }
        String violationMessages = getViolationMessages(violations);
        throw new CustomException(violationMessages);
    }

    @Override
    public List<Technician> findAll() {
        return baseRepository.findAll();
    }

    protected void validateImage(String imageAddress) {
        log.info("Validating image [{}]", imageAddress);
        try {
            File imageFile = new File(imageAddress);
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile);

            if (imageInputStream != null) {
                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
                ImageReader reader = imageReaders.next();
                String imageType = reader.getFormatName();
                long imageSize = imageFile.length() / 1024; // Size in KB

                if (!imageType.equalsIgnoreCase("jpeg")) {
                    log.error("Image format [{}] is invalid throwing exception", imageType);
                    throw new CustomException("The only supported format is JPEG");
                } else if (imageSize > 300) {
                    log.error("Image size [{}] is more than 300kb throwing exception", imageSize);
                    throw new CustomException("Max image size is 300kb");
                }
            } else {
                log.error("Can't find image throwing exception");
                throw new NotFoundException("ImageAddress: " + imageAddress);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    protected String getViolationMessages(Set<ConstraintViolation<RegisterDto>> violations) {
        log.error("RegisterDto violates some fields throwing exception");
        StringBuilder messageBuilder = new StringBuilder();
        for (ConstraintViolation<RegisterDto> violation : violations) {
            messageBuilder.append("\n").append(violation.getMessage());
        }
        return messageBuilder.toString().trim();
    }

    @Override
    public void confirmTechnician(Long technicianId) {
        log.info("Confirming technician");
        Technician technician = findById(technicianId);

        try {
            log.info("Connecting to [{}]", baseRepository);
            technician.setStatus(TechnicianStatus.CONFIRMED);
            baseRepository.save(technician);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public void saveImage(Long technicianId, String imageAddress) {

        Technician technician = findById(technicianId);

        log.info("Adding image to [{}] profile", technician.getEmail());

        validateImage(imageAddress);
        byte[] bytes = imageToBytes(imageAddress);
        technician.setImageData(bytes);
        try {
            baseRepository.save(technician);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
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
            throw new DuplicateValueException("Email address already exists in the database");
        }
    }

    protected Technician mapDtoValues(RegisterDto registerDto) {
        log.info("Mapping [{}] values", registerDto);
        Technician technician = new Technician();
        technician.setFirstname(registerDto.getFirstname());
        technician.setLastname(registerDto.getLastname());
        technician.setEmail(registerDto.getEmailAddress());
        technician.setPassword(registerDto.getPassword());
        technician.setScore(0);
        technician.setStatus(TechnicianStatus.NEW);
        return technician;
    }
}
