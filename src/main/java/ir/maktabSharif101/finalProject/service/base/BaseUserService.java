package ir.maktabSharif101.finalProject.service.base;

import ir.maktabSharif101.finalProject.base.service.BaseEntityService;
import ir.maktabSharif101.finalProject.entity.User;

import java.util.Optional;

public interface BaseUserService<T extends User> extends BaseEntityService<T,Long> {
 boolean existsByEmailAddress(String emailAddress);
 Optional<T> findByEmailAddress(String emailAddress);
 Optional<T> login(String emailAddress,String password);

}