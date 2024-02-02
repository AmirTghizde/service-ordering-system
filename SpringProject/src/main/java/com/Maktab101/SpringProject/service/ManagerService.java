package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.Manager;
import com.Maktab101.SpringProject.service.base.BaseUserService;
import com.Maktab101.SpringProject.dto.RegisterDto;

public interface ManagerService extends BaseUserService<Manager> {
    Manager register(RegisterDto registerDto);
}
