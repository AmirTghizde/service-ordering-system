package com.Maktab101.SpringProject.repository;

import com.Maktab101.SpringProject.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository
        extends JpaRepository<EmailVerification,Long> {

    EmailVerification findByToken(String token);
    void deleteByToken(String token);

}
