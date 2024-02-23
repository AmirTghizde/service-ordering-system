package com.Maktab101.SpringProject.service.base;

import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface BaseUserService<T extends User>{
    boolean existsByEmailAddress(String emailAddress);
    T findByEmailAddress(String emailAddress);
    void editPassword(Long userId, String newPassword);
    T findById(Long userId);
    T save(T t);
}