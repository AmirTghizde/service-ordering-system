package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.entity.Technician;
import ir.maktabSharif101.finalProject.entity.enums.TechnicianStatus;
import ir.maktabSharif101.finalProject.repository.TechnicianRepository;
import ir.maktabSharif101.finalProject.service.TechnicianService;
import ir.maktabSharif101.finalProject.service.base.BaseUserServiceImpl;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;
import ir.maktabSharif101.finalProject.utils.CustomException;
import ir.maktabSharif101.finalProject.utils.Validation;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.PersistenceException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Iterator;

public class TechnicianServiceImpl extends BaseUserServiceImpl<Technician, TechnicianRepository> implements TechnicianService {
    public TechnicianServiceImpl(TechnicianRepository baseRepository) {
        super(baseRepository);
    }

    @Override
    public Technician register(RegisterDto registerDto, String imageAddress) {
        validateInfo(registerDto,imageAddress);
        checkCondition(registerDto);
        Technician technician = mapDtoValues(registerDto, imageAddress);

        try {
            return baseRepository.save(technician);
        } catch (PersistenceException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return technician;
    }

    private void validateInfo(RegisterDto registerDto, String imageAddress) {
        if (!Validation.isValidName(registerDto.getFirstname())|| !Validation.isValidName(registerDto.getLastname())){
            throw new CustomException("4422","Invalid usage of numbers in name");
        } else if (!Validation.isValidEmail(registerDto.getEmailAddress())) {
            throw new CustomException("4422","Invalid email");
        }else if (!Validation.isValidPassword(registerDto.getPassword())) {
            throw new CustomException("4422", """
                    Passwords must contain:\s
                    At least 1 number
                    At least 1 big letter
                    At least 1 small letter
                    At least one of these symbols  # $ % &
                    And at least 8 characters""");
        }
        //image validation
        try {
            File imageFile = new File(imageAddress);
            BufferedImage image = ImageIO.read(imageFile);

            if (image != null) {
                ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile);
                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
                ImageReader reader = imageReaders.next();
                String imageType = reader.getFormatName();
                System.out.println(imageType);
                long imageSize = imageFile.length() / 1024; // Size in KB
                System.out.println(imageSize);


                if (!imageType.equalsIgnoreCase("jpeg")){
                    throw new CustomException("4422", "Invalid image format");
                } else if (imageSize > 300) {
                    throw new CustomException("4422", "Invalid image size");
                }
            } else {
                throw new CustomException("4004", "Image file not found");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private byte[] imageToBytes(String imageAddress) {
        try {
            File imageFile = new File(imageAddress);
            BufferedImage image = ImageIO.read(imageFile);
            return Files.readAllBytes(imageFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected void checkCondition(RegisterDto registerDto) {
        if (existsByEmailAddress(registerDto.getEmailAddress())) {
            throw new CustomException("4499", "Duplicate email address");
        }
    }

    protected Technician mapDtoValues(RegisterDto registerDto, String imageAddress) {
        Technician technician = new Technician();
        technician.setFirstname(registerDto.getFirstname());
        technician.setLastname(registerDto.getLastname());
        technician.setEmailAddress(registerDto.getEmailAddress());
        technician.setPassword(registerDto.getPassword());
        technician.setImageData(imageToBytes(imageAddress));
        technician.setScore(0);
        technician.setStatus(TechnicianStatus.NEW);
        return technician;
    }
}
