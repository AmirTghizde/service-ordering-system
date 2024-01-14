package ir.maktabSharif101.finalProject.repository.impl;

import ir.maktabSharif101.finalProject.entity.Manager;
import ir.maktabSharif101.finalProject.repository.ManagerRepository;
import ir.maktabSharif101.finalProject.repository.base.BaseUserRepositoryImpl;

import javax.persistence.EntityManager;

public class ManagerRepositoryImpl extends BaseUserRepositoryImpl<Manager> implements ManagerRepository {
    public ManagerRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Manager> getEntityClass() {
        return Manager.class;
    }
}
