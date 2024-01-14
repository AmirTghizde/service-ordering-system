package ir.maktabSharif101.finalProject.repository.impl;

import ir.maktabSharif101.finalProject.base.repository.BaseEntityRepositoryImpl;
import ir.maktabSharif101.finalProject.entity.Order;
import ir.maktabSharif101.finalProject.repository.OrderRepository;

import javax.persistence.EntityManager;

public class OrderRepositoryImpl extends BaseEntityRepositoryImpl<Order,Long> implements OrderRepository {
    public OrderRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Order> getEntityClass() {
        return Order.class;
    }
}





