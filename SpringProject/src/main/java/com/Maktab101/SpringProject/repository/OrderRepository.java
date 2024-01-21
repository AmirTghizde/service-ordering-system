package com.Maktab101.SpringProject.repository;

import com.Maktab101.SpringProject.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
