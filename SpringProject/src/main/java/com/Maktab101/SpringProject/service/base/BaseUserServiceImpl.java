package com.Maktab101.SpringProject.service.base;

import com.Maktab101.SpringProject.model.User;
import com.Maktab101.SpringProject.repository.base.BaseUserRepository;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import com.Maktab101.SpringProject.utils.exceptions.NotFoundException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public abstract class BaseUserServiceImpl<T extends User>
        implements BaseUserService<T> {

    protected final BaseUserRepository<T> baseRepository;

    @Autowired
    public BaseUserServiceImpl(BaseUserRepository<T> baseUserRepository) {
        this.baseRepository = baseUserRepository;
    }

    @Override
    public boolean existsByEmailAddress(String emailAddress) {
        log.info("trying to check if [{}] exists", emailAddress);
        return baseRepository.existsByEmail(emailAddress);
    }

    @Override
    public T findByEmailAddress(String emailAddress) {
        log.info("trying to find [{}]", emailAddress);
        return baseRepository.findByEmail(emailAddress).orElseThrow(
                () -> new NotFoundException("Couldn't find a user with this email: " + emailAddress));
    }

    @Override
    public T login(String emailAddress, String password) {
        log.info("Logging in with this data [email:{}, password{}]", emailAddress, password);
        if (baseRepository.existsByEmailAndPassword(emailAddress, password)) {
            T user = findByEmailAddress(emailAddress);
            if (user == null) {
                throw new NotFoundException("Couldn't find user user is null");
            }
            log.info("[{}] successfully longed in", user.getEmail());
            return user;
        }
        throw new NotFoundException("User not found check the email and password");
    }

    @Override
    public void editPassword(Long userId, String newPassword) {

        T t = findById(userId);
        log.info("[{}] is trying to change password from [{}] to [{}]",
                t.getEmail(), t.getPassword(), newPassword);
        if (!StringUtils.isBlank(newPassword)) {
            try {
                t.setPassword(newPassword);
                baseRepository.save(t);
            } catch (PersistenceException e) {
                log.error("PersistenceException occurred throwing CustomException ... ");
                throw new CustomException(e.getMessage());
            }
        } else {
            log.error("Password is empty throwing exception");
            throw new CustomException("Password must not be blank");
        }

    }

    @Override
    public T findById(Long userId) {
        return baseRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Couldn't find a user with his id: " + userId));
    }

    @Override
    public T save(T t) {
        return baseRepository.save(t);
    }
}
