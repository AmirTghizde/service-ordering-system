package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.dto.users.RequestDto;
import com.Maktab101.SpringProject.model.*;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.repository.OrderRepository;
import com.Maktab101.SpringProject.dto.order.OrderSubmitDto;
import com.Maktab101.SpringProject.service.*;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import com.Maktab101.SpringProject.utils.exceptions.NotFoundException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final SubServicesService subServicesService;
    private final CustomerService customerService;
    private final OrderRepository orderRepository;
    private final TechnicianService technicianService;
    private final FilterSpecification<Order> filterSpecification;

    @Autowired
    public OrderServiceImpl(SubServicesService subServicesService, CustomerService customerService,
                            OrderRepository orderRepository, TechnicianService technicianService,
                            FilterSpecification<Order> filterSpecification) {
        this.subServicesService = subServicesService;
        this.customerService = customerService;
        this.orderRepository = orderRepository;
        this.technicianService = technicianService;
        this.filterSpecification = filterSpecification;
    }


    @Override
    @Transactional
    public void submitOrder(Long customerId, OrderSubmitDto orderSubmitDto) {
        log.info("Customer with id [{}] is trying to submit a new order [{}]", customerId, orderSubmitDto);
        SubServices subServices = subServicesService.findById(orderSubmitDto.getSubServiceId());
        Customer customer = customerService.findById(customerId);

        checkCondition(orderSubmitDto, subServices);
        Order order = mapDtoValues(orderSubmitDto);
        updateFields(order,subServices,customer);

        try {
            log.info("Connecting to [{}]", orderRepository);
            customerService.save(customer);
            orderRepository.save(order);
            subServicesService.save(subServices);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<Order> findAwaitingOrdersByTechnician(Long technicianId) {
        log.info("Finding orders for technician[{}]", technicianId);
        Technician technician = technicianService.findById(technicianId);

        List<SubServices> subServices = technician.getSubServices();

        List<OrderStatus> orderStatuses = Arrays.asList(
                OrderStatus.AWAITING_TECHNICIAN_SUGGESTION,
                OrderStatus.AWAITING_TECHNICIAN_SELECTION
        );
        return orderRepository.findBySubServicesInAndOrderStatusIn(subServices, orderStatuses);
    }

    @Override
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("Couldn't find an order with this id: " + orderId));
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void startOrder(Long orderId) {
        log.info("Starting order [{}]", orderId);
        Order order = findById(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.AWAITING_TECHNICIAN_ARRIVAL)) {
            log.error("Invalid order status throwing exception");
            throw new CustomException("You can't start this order");
        }
        order.setOrderStatus(OrderStatus.STARTED);
        save(order);
    }

    @Override
    public void finishOrder(Long orderId, double point) {
        log.info("Finishing order [{}]", orderId);
        Order order = findById(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.STARTED)) {
            log.error("Invalid order status throwing exception");
            throw new CustomException("You can't finish this order");
        }

        if (0 <= point && point <= 5) {
            order.setOrderStatus(OrderStatus.FINISHED);
            order.setPoint(point);
            save(order);
        } else {
            throw new CustomException("Point must be between 0 to 5");
        }
    }

    @Override
    public void addComment(Long orderId, String comment) {
        log.info("Adding comment for this order [{}]", orderId);
        Order order = findById(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.FINISHED)) {
            log.error("Invalid order status throwing exception");
            throw new CustomException("Can't leave a comment now");
        }
        order.setComment(comment);
        save(order);
    }

    @Override
    public int getNumberCaptcha() {
        Random random = new Random();
        return random.nextInt(999999 - 100000 + 1) + 100000;
    }

    @Override
    public List<Order> handelFiltering(RequestDto requestDto) {
        Specification<Order> specificationList = filterSpecification.getSpecificationList(
                requestDto.getSearchRequestDto(),
                requestDto.getGlobalOperator());

        return orderRepository.findAll(specificationList);
    }

    protected void checkCondition(OrderSubmitDto orderSubmitDto, SubServices subServices) {
        log.info("Checking order conditions");
        if (orderSubmitDto.getPrice() < subServices.getBaseWage()) {
            log.error("Price is lower than base wage throwing exception");
            throw new CustomException("Price can't be lower than base wage");
        }
        LocalDateTime localDateTime = convertDateAndTime(orderSubmitDto.getTime(), orderSubmitDto.getDate());
        LocalDateTime now = LocalDateTime.now();
        if (localDateTime.isBefore(now)) {
            log.error("Date is before now throwing exception");
            throw new CustomException("Date and time can't be before now");
        }
    }

    protected Order mapDtoValues(OrderSubmitDto orderSubmitDto) {
        log.info("Mapping [{}] values", orderSubmitDto);
        Order order = new Order();
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);
        order.setJobInfo(orderSubmitDto.getJobInfo());
        order.setAddress(orderSubmitDto.getAddress());
        order.setPrice(orderSubmitDto.getPrice());
        order.setPoint(0);

        LocalDateTime localDateTime = convertDateAndTime(orderSubmitDto.getTime(), orderSubmitDto.getDate());

        order.setDateAndTime(localDateTime);
        return order;
    }

    protected LocalDateTime convertDateAndTime(String time, String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);

        return localDate.atTime(localTime);
    }

    private void updateFields(Order order, SubServices subServices, Customer customer) {
        customer.getOrders().add(order);
        subServices.getOrders().add(order);
        order.setSubServices(subServices);
        order.setCustomer(customer);
    }

}
