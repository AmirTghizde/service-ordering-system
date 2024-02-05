package com.Maktab101.SpringProject.repository.base;


import com.Maktab101.SpringProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BaseUserRepository<T extends User> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    boolean existsByEmail(String email);
    Optional<T> findByEmail(String email);
    boolean existsByEmailAndPassword(String emailAddress, String password);

}
