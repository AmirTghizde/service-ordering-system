package com.Maktab101.SpringProject.service;

import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.service.base.BaseUserService;
import com.Maktab101.SpringProject.dto.users.RegisterDto;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TechnicianService extends BaseUserService<Technician> {
    Technician register(RegisterDto registerDto);

    List<Technician> findAll();

    void confirmTechnician(Long technicianId);

    void saveImage(Long technicianId, String imageAddress);

    List<Technician> filter(Specification<Technician> specification);
}
