package com.Maktab101.SpringProject.service.base;

import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface BaseUserService<T extends User>{
    boolean existsByEmailAddress(String emailAddress);

    Optional<T> findByEmailAddress(String emailAddress);

    T login(String emailAddress, String password);

    void editPassword(Long userId, String newPassword);
    Optional<T> findById(Long userId);

    T save(T t);

}