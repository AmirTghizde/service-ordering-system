package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.*;
import com.Maktab101.SpringProject.service.base.BaseUserServiceImpl;
import com.Maktab101.SpringProject.utils.exceptions.NotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import com.Maktab101.SpringProject.repository.EmailVerificationRepository;
import com.Maktab101.SpringProject.service.EmailVerificationService;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Slf4j
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = StandardCharsets.US_ASCII;

    @Autowired
    public EmailVerificationServiceImpl(EmailVerificationRepository emailVerificationRepository, JavaMailSender mailSender) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.mailSender = mailSender;
    }

    @Override
    public EmailVerification findByToken(String token) {
        return emailVerificationRepository.findByToken(token);
    }

    @Override
    public void deleteByToken(String token) {
        emailVerificationRepository.deleteByToken(token);
    }

    @Override
    public EmailVerification generateToken(User user) {

        String tokenValue = new String(Base64.getUrlEncoder()
                .encode(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setToken(tokenValue);
        emailVerification.setUser(user);

        try {
            return emailVerificationRepository.save(emailVerification);
        } catch (PersistenceException e) {
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public void sendEmail(EmailVerification emailVerification) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailVerification.getUser().getEmail());
            helper.setSubject("Confirm your email");
            helper.setText("<html>" +
                            "<body>" +
                            "<h2> Dear " + emailVerification.getUser().getFirstname() + ",</h2>" +
                            "<br/> Please click on the link below to so we can get you registered" +
                            "<br/> " + generateLink(emailVerification.getToken(),emailVerification.getUser()) +
                            "<br/> Regards, " +
                            "<br/> Registration team" +
                            "</body>" +
                            "</html>"
                    , true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean isValidToken(String token) {
        EmailVerification emailVerification = findByToken(token);
        return emailVerification != null && token.equals(emailVerification.getToken());
    }

    private String generateLink(String token,User user) {
        return "<a href=http://localhost:8080/" +findUser(user)+ "/confirmEmail?token="+ token + ">Confirm Email</a>";
    }

    private String findUser(User user) {
        if (user instanceof Customer) {
            return "customers";
        } else if (user instanceof Technician) {
            return "technicians";
        } else if (user instanceof Manager) {
            return "manager";
        } else {
            throw new NotFoundException("Couldn't find the user for this token");
        }
    }
}
