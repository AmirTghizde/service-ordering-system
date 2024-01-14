package ir.maktabSharif101.finalProject.repository.impl;

import ir.maktabSharif101.finalProject.base.repository.BaseEntityRepositoryImpl;
import ir.maktabSharif101.finalProject.entity.MainServices;
import ir.maktabSharif101.finalProject.repository.MainServicesRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class MainServicesRepositoryImpl extends BaseEntityRepositoryImpl<MainServices, Long> implements MainServicesRepository {
    public MainServicesRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<MainServices> getEntityClass() {
        return MainServices.class;
    }

    @Override
    public Optional<MainServices> findByName(String mainServiceName) {
        TypedQuery<MainServices> query = entityManager.createQuery(
                "select s from MainServices s where s.name = :name",
                getEntityClass()
        );
        query.setParameter("name", mainServiceName);
        List<MainServices> resultList = query.getResultList();
        return resultList.stream().findFirst();
    }

    @Override
    public boolean existsByName(String mainServiceName) {
        TypedQuery<Long> query = entityManager.createQuery(
                "select count(s) from MainServices s where s.name = :name",
                Long.class
        );
        query.setParameter("name", mainServiceName);
        return query.getSingleResult() > 0;
    }
}
