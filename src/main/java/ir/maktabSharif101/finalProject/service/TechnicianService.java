package ir.maktabSharif101.finalProject.service;


import ir.maktabSharif101.finalProject.entity.Customer;
import ir.maktabSharif101.finalProject.entity.Technician;
import ir.maktabSharif101.finalProject.service.base.BaseUserService;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;

public interface TechnicianService extends BaseUserService<Technician> {
    Technician register (RegisterDto registerDto,String imageAddress);
    void confirmTechnician(Long technicianId);
}
