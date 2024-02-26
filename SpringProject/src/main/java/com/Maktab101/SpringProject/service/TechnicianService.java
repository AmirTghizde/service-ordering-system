package com.Maktab101.SpringProject.service;

import com.Maktab101.SpringProject.dto.users.RequestDto;
import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.service.base.BaseUserService;
import com.Maktab101.SpringProject.dto.users.RegisterDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TechnicianService extends BaseUserService<Technician> {
    Technician register(RegisterDto registerDto);

    List<Technician> findAll();

    void confirmTechnician(Long technicianId);

    void saveImage(Long technicianId, byte[] imageData);

    List<Technician> handelFiltering(RequestDto requestDto);

    void addCredit(Long technicianId, double amount);

    void addPoints(Long technicianId, double amount);

    void reducePoints(Long technicianId, double amount);

    RequestDto getRequestDto(Long technicianId, OrderStatus status);

    void verify(Long technicianId, String token);
}
