package com.Maktab101.SpringProject.service;

import com.Maktab101.SpringProject.model.EmailVerification;
import com.Maktab101.SpringProject.model.User;

public interface EmailVerificationService {
    EmailVerification findByToken(String token);
    void deleteByToken(String token);
    EmailVerification generateToken (User user);
    void sendEmail(EmailVerification emailVerification);
    boolean isValidToken(String token);
}
