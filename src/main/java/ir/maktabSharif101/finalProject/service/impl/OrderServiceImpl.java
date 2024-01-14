package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.base.service.BaseEntityServiceImpl;
import ir.maktabSharif101.finalProject.entity.Order;
import ir.maktabSharif101.finalProject.repository.OrderRepository;
import ir.maktabSharif101.finalProject.service.OrderService;

public class OrderServiceImpl extends BaseEntityServiceImpl<Order,Long, OrderRepository>
implements OrderService {
    public OrderServiceImpl(OrderRepository baseRepository) {
        super(baseRepository);
    }
}
