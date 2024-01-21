package com.Maktab101.SpringProject.service;

import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.service.base.BaseUserService;
import com.Maktab101.SpringProject.service.dto.RegisterDto;

import java.util.Optional;

public interface TechnicianService extends BaseUserService<Technician> {
    Technician register (RegisterDto registerDto);
    void confirmTechnician(Long technicianId);
    void saveImage(Long technicianId,String imageAddress);
}
