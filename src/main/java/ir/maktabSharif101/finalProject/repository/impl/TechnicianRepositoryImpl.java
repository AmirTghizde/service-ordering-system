package ir.maktabSharif101.finalProject.repository.impl;

import ir.maktabSharif101.finalProject.entity.Technician;
import ir.maktabSharif101.finalProject.repository.TechnicianRepository;
import ir.maktabSharif101.finalProject.repository.base.BaseUserRepositoryImpl;

import javax.persistence.EntityManager;

public class TechnicianRepositoryImpl extends BaseUserRepositoryImpl<Technician> implements TechnicianRepository {
    public TechnicianRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Technician> getEntityClass() {
        return Technician.class;
    }
}
