package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.base.service.BaseEntityServiceImpl;
import ir.maktabSharif101.finalProject.entity.Customer;
import ir.maktabSharif101.finalProject.entity.Order;
import ir.maktabSharif101.finalProject.entity.SubServices;
import ir.maktabSharif101.finalProject.entity.enums.OrderStatus;
import ir.maktabSharif101.finalProject.repository.OrderRepository;
import ir.maktabSharif101.finalProject.service.CustomerService;
import ir.maktabSharif101.finalProject.service.OrderService;
import ir.maktabSharif101.finalProject.service.SubServicesService;
import ir.maktabSharif101.finalProject.service.dto.OrderSubmitDto;
import ir.maktabSharif101.finalProject.utils.CustomException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Slf4j
public class OrderServiceImpl extends BaseEntityServiceImpl<Order, Long, OrderRepository>
        implements OrderService {

    private final SubServicesService subServicesService;
    private final CustomerService customerService;
    private final Validator validator;


    public OrderServiceImpl(OrderRepository baseRepository, SubServicesService subServicesService,
                            CustomerService customerService, Validator validator) {
        super(baseRepository);
        this.subServicesService = subServicesService;
        this.customerService = customerService;
        this.validator = validator;
    }

    @Override
    public void submitOrder(Long customerId, OrderSubmitDto orderSubmitDto) {
        log.info("Customer with id [{}] is trying to submit a new order {}", customerId, orderSubmitDto);
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
                log.info("Connecting to [{}]", baseRepository);
                baseRepository.beginTransaction();
                customer.getOrders().add(order);
                subServices.getOrders().add(order);
                order.setSubServices(subServices);
                order.setCustomer(customer);
                customerService.save(customer);
                baseRepository.save(order);
                subServicesService.save(subServices);
                baseRepository.commitTransaction();
                return;
            } catch (PersistenceException e) {
                baseRepository.rollbackTransaction();
                System.out.println(e.getMessage());
            }
        }
        String violationMessages = getViolationMessages(violations);
        throw new CustomException("ValidationException", violationMessages);
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
