package com.Maktab101.SpringProject.repository;

import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.model.SubServices;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long>, JpaSpecificationExecutor<Order> {

    List<Order> findBySubServicesInAndOrderStatusIn(List<SubServices> subServices, List<OrderStatus> orderStatuses);

}
