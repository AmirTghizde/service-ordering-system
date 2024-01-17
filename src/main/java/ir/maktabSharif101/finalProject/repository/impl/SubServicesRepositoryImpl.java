package ir.maktabSharif101.finalProject.repository.impl;

import ir.maktabSharif101.finalProject.base.repository.BaseEntityRepositoryImpl;
import ir.maktabSharif101.finalProject.entity.SubServices;
import ir.maktabSharif101.finalProject.repository.SubServicesRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class SubServicesRepositoryImpl extends BaseEntityRepositoryImpl<SubServices, Long> implements SubServicesRepository {
    public SubServicesRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<SubServices> getEntityClass() {
        return SubServices.class;
    }

    @Override
    public Optional<SubServices> findByName(String subServiceName) {
        TypedQuery<SubServices> query = entityManager.createQuery(
                "select s from SubServices s where s.name = :name",
                getEntityClass()
        );
        query.setParameter("name", subServiceName);
        List<SubServices> resultList = query.getResultList();
        return resultList.stream().findFirst();
    }

    @Override
    public boolean existsByName(String subServiceName) {
        TypedQuery<Long> query = entityManager.createQuery(
                "select count(s) from SubServices s where s.name = :name",
                Long.class
        );
        query.setParameter("name", subServiceName);
        return query.getSingleResult() > 0;
    }
}
