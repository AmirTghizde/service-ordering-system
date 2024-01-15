package ir.maktabSharif101.finalProject.service;

import ir.maktabSharif101.finalProject.entity.Manager;
import ir.maktabSharif101.finalProject.service.base.BaseUserService;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;

public interface ManagerService extends BaseUserService<Manager> {
    Manager register(RegisterDto registerDto);
}
