package ir.maktabSharif101.finalProject.repository.base;

import ir.maktabSharif101.finalProject.base.repository.BaseEntityRepositoryImpl;
import ir.maktabSharif101.finalProject.entity.MainServices;
import ir.maktabSharif101.finalProject.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public abstract class BaseUserRepositoryImpl<T extends User> extends BaseEntityRepositoryImpl<T, Long> implements
        BaseUserRepository<T> {
    public BaseUserRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public boolean existsByEmailAddress(String emailAddress) {
        TypedQuery<Long> query = entityManager.createQuery(
                "select count(s) from " + getEntityClass().getSimpleName() + " s where s.emailAddress = :email",
                Long.class
        );
        query.setParameter("email", emailAddress);
        return query.getSingleResult() > 0;
    }

    @Override
    public Optional<T> findByEmailAddress(String emailAddress) {
        TypedQuery<T> query = entityManager.createQuery(
                "select s from " + getEntityClass().getSimpleName() + " s where s.emailAddress = :email",
                getEntityClass()
        );
        query.setParameter("email", emailAddress);
        List<T> resultList = query.getResultList();
        return resultList.stream().findFirst();
    }

    @Override
    public boolean existsByEmailAndPass(String emailAddress, String password) {
        TypedQuery<Long> query = entityManager.createQuery(
                "select count(s) from " + getEntityClass().getSimpleName() + " s where s.emailAddress = :email" +
                        " and s.password = :password",
                Long.class
        );
        query.setParameter("email", emailAddress);
        query.setParameter("password", password);
        return query.getSingleResult() > 0;
    }
}
