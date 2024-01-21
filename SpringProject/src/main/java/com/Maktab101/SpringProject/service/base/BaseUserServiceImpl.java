package com.Maktab101.SpringProject.service.base;

import com.Maktab101.SpringProject.model.User;
import com.Maktab101.SpringProject.repository.base.BaseUserRepository;
import com.Maktab101.SpringProject.utils.CustomException;
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
    public Optional<T> findByEmailAddress(String emailAddress) {
        log.info("trying to find [{}]", emailAddress);
        return baseRepository.findByEmail(emailAddress);
    }

    @Override
    public T login(String emailAddress, String password) {
        log.info("Logging in with this data [email:{}, password{}]", emailAddress, password);
        if (baseRepository.existsByEmailAndPassword(emailAddress, password)) {
            T user = findByEmailAddress(emailAddress).orElse(null);
            if (user == null) {
                throw new CustomException("UserNotFound", "We can't find the user");
            }
            log.info("[{}] successfully longed in", user.getEmail());
            return user;
        }
        throw new CustomException("UserNotFound", "Check email or password");
    }

    @Override
    public void editPassword(Long userId, String newPassword) {

        T t = baseRepository.findById(userId).orElseThrow(
                () -> new CustomException("UserNotFound", "We can't find the user"));
        log.info("[{}] is trying to change password from [{}] to [{}]",
                t.getEmail(), t.getPassword(), newPassword);
        if (!StringUtils.isBlank(newPassword)) {
            try {
                t.setPassword(newPassword);
                baseRepository.save(t);
            } catch (PersistenceException e) {
                System.out.println(e.getMessage());
            }
        } else {
            log.error("Password is empty throwing exception");
            throw new CustomException("invalidPassword", "Password must not be blank");
        }

    }

    @Override
    public Optional<T> findById(Long userId) {
        return baseRepository.findById(userId);
    }

    @Override
    public T save(T t) {
        return baseRepository.save(t);
    }
}
