package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.model.SubServices;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.repository.OrderRepository;
import com.Maktab101.SpringProject.service.CustomerService;
import com.Maktab101.SpringProject.service.OrderService;
import com.Maktab101.SpringProject.service.SubServicesService;
import com.Maktab101.SpringProject.service.TechnicianService;
import com.Maktab101.SpringProject.service.dto.OrderSubmitDto;
import com.Maktab101.SpringProject.utils.CustomException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final SubServicesService subServicesService;
    private final CustomerService customerService;
    private final OrderRepository orderRepository;
    private final TechnicianService technicianService;
    private final Validator validator;

    @Autowired
    public OrderServiceImpl(SubServicesService subServicesService, CustomerService customerService,
                            OrderRepository orderRepository, TechnicianService technicianService,
                            Validator validator) {
        this.subServicesService = subServicesService;
        this.customerService = customerService;
        this.orderRepository = orderRepository;
        this.technicianService = technicianService;
        this.validator = validator;
    }


    @Override
    @Transactional
    public void submitOrder(Long customerId, OrderSubmitDto orderSubmitDto) {
        log.info("Customer with id [{}] is trying to submit a new order [{}]", customerId, orderSubmitDto);
        Set<ConstraintViolation<OrderSubmitDto>> violations = validator.validate(orderSubmitDto);
        if (violations.isEmpty()) {
            log.info("Information is validated - commencing registration");
            SubServices subServices = subServicesService.findById(orderSubmitDto.getSubServiceId()).orElseThrow(() ->
                    new CustomException("SubServiceNotFound", "We can not find the sub service"));

            Customer customer = customerService.findById(customerId).orElseThrow(
                    () -> new CustomException("CustomerNotFound", "We can not find this customer"));

            checkCondition(orderSubmitDto, subServices);
            Order order = mapDtoValues(orderSubmitDto);

            try {
                log.info("Connecting to [{}]", orderRepository);
                customer.getOrders().add(order);
                subServices.getOrders().add(order);
                order.setSubServices(subServices);
                order.setCustomer(customer);
                customerService.save(customer);
                orderRepository.save(order);
                subServicesService.save(subServices);
                return;
            } catch (PersistenceException e) {
                System.out.println(e.getMessage());
            }
        }
        String violationMessages = getViolationMessages(violations);
        throw new CustomException("ValidationException", violationMessages);
    }

    @Override
    @Transactional
    public List<Order> findAwaitingOrdersByTechnician(Long technicianId) {
        Technician technician = technicianService.findById(technicianId).orElseThrow(() ->
                new CustomException("TechnicianNotFound", "We can't find the technician"));

        List<SubServices> subServices = technician.getSubServices();

        List<OrderStatus> orderStatuses = Arrays.asList(
                OrderStatus.AWAITING_TECHNICIAN_SUGGESTION,
                OrderStatus.AWAITING_TECHNICIAN_SELECTION
        );
        return orderRepository.findBySubServicesInAndOrderStatusIn(subServices,orderStatuses);
    }

    private String getViolationMessages(Set<ConstraintViolation<OrderSubmitDto>> violations) {
        log.error("SubmitOrderDto violates some fields throwing exception");
        StringBuilder messageBuilder = new StringBuilder();
        for (ConstraintViolation<OrderSubmitDto> violation : violations) {
            messageBuilder.append("\n").append(violation.getMessage());
        }
        return messageBuilder.toString().trim();
    }

    protected void checkCondition(OrderSubmitDto orderSubmitDto, SubServices subServices) {
        log.info("Checking registration conditions");
        if (orderSubmitDto.getPrice() < subServices.getBaseWage()) {
            log.error("Price is lower than base wage throwing exception");
            throw new CustomException("InvalidPrice", "Price can't be lower than base wage");
        }
        LocalDateTime localDateTime = convertDateAndTime(orderSubmitDto.getTime(), orderSubmitDto.getDate());
        LocalDateTime now = LocalDateTime.now();
        if (localDateTime.isBefore(now)) {
            log.error("Date is before now throwing exception");
            throw new CustomException("InvalidDateAndTime", "Date and time can't be before now");
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

    private LocalDateTime convertDateAndTime(String time, String date) {
        log.info("Converting date and time");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);

        return localDate.atTime(localTime);
    }

}
