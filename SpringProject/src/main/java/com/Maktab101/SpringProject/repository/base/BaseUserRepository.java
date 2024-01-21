package com.Maktab101.SpringProject.repository.base;


import com.Maktab101.SpringProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseUserRepository<T extends User> extends JpaRepository<T, Long> {
    boolean existsByEmail(String email);
    Optional<T> findByEmail(String email);
    boolean existsByEmailAndPassword(String emailAddress, String password);

}
