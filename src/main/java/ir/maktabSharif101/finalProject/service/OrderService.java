package ir.maktabSharif101.finalProject.service;

import ir.maktabSharif101.finalProject.base.service.BaseEntityService;
import ir.maktabSharif101.finalProject.entity.Order;
import ir.maktabSharif101.finalProject.service.dto.OrderSubmitDto;

public interface OrderService extends BaseEntityService<Order,Long> {
    void submitOrder(Long customerId,OrderSubmitDto orderSubmitDto);
}
