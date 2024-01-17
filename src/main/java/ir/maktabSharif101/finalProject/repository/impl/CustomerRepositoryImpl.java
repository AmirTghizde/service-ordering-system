package ir.maktabSharif101.finalProject.repository.impl;

import ir.maktabSharif101.finalProject.entity.Customer;
import ir.maktabSharif101.finalProject.repository.CustomerRepository;
import ir.maktabSharif101.finalProject.repository.base.BaseUserRepositoryImpl;

import javax.persistence.EntityManager;

public class CustomerRepositoryImpl extends BaseUserRepositoryImpl<Customer> implements CustomerRepository {
    public CustomerRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Customer> getEntityClass() {
        return Customer.class;
    }
}
