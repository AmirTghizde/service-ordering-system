package com.Maktab101.SpringProject.service;

import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.service.base.BaseUserService;
import com.Maktab101.SpringProject.dto.RegisterDto;

import java.util.List;

public interface TechnicianService extends BaseUserService<Technician> {
    Technician register (RegisterDto registerDto);
    List<Technician> findAll ();
    void confirmTechnician(Long technicianId);
    void saveImage(Long technicianId,String imageAddress);
}
