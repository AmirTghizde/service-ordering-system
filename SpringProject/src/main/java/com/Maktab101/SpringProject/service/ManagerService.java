package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.Manager;
import com.Maktab101.SpringProject.service.base.BaseUserService;
import com.Maktab101.SpringProject.dto.users.RegisterDto;

public interface ManagerService extends BaseUserService<Manager> {
    Manager register(RegisterDto registerDto);
    void verify(Long managerId, String token);
}
