package ir.maktabSharif101.finalProject.service.base;

import ir.maktabSharif101.finalProject.base.service.BaseEntityServiceImpl;
import ir.maktabSharif101.finalProject.entity.User;
import ir.maktabSharif101.finalProject.repository.base.BaseUserRepository;
import ir.maktabSharif101.finalProject.utils.CustomException;

import javax.persistence.PersistenceException;
import java.util.Optional;

public abstract class BaseUserServiceImpl<T extends User, R extends BaseUserRepository<T>>
        extends BaseEntityServiceImpl<T, Long, R> implements BaseUserService<T> {
    public BaseUserServiceImpl(R baseRepository) {
        super(baseRepository);
    }

    @Override
    public boolean existsByEmailAddress(String emailAddress) {
        return baseRepository.existsByEmailAddress(emailAddress);
    }

    @Override
    public Optional<T> findByEmailAddress(String emailAddress) {
        return baseRepository.findByEmailAddress(emailAddress);
    }

    @Override
    public T login(String emailAddress, String password) {
        if (baseRepository.existsByEmailAndPass(emailAddress, password)) {
            T user = findByEmailAddress(emailAddress).orElse(null);
            if (user ==null){
              throw new CustomException("UserNotFound","We can't find the user");
            }
            return user;
        }
        throw new CustomException("UserNotFound", "Check email or password");
    }

    @Override
    public void editPassword(Long userId, String newPassword) {
        T t = baseRepository.findById(userId).orElseThrow(
                () -> new CustomException("UserNotFound", "We can't find the user"));

        try{
            t.setPassword(newPassword);
            baseRepository.save(t);
        }catch (PersistenceException e){
            System.out.println(e.getMessage());
        }
    }
}
