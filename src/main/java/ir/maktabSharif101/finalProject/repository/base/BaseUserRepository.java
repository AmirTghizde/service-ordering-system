package ir.maktabSharif101.finalProject.repository.base;

import ir.maktabSharif101.finalProject.base.repository.BaseEntityRepository;
import ir.maktabSharif101.finalProject.entity.User;

import java.util.Optional;

public interface BaseUserRepository<T extends User> extends BaseEntityRepository<T,Long> {
    boolean existsByEmailAddress(String emailAddress);
    Optional<T> findByEmailAddress(String emailAddress);
    boolean existsByEmailAndPass(String emailAddress, String password);
}
