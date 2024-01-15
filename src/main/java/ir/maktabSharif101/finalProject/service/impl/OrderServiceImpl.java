package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.base.service.BaseEntityServiceImpl;
import ir.maktabSharif101.finalProject.entity.Customer;
import ir.maktabSharif101.finalProject.entity.Order;
import ir.maktabSharif101.finalProject.entity.SubServices;
import ir.maktabSharif101.finalProject.entity.enums.OrderStatus;
import ir.maktabSharif101.finalProject.repository.OrderRepository;
import ir.maktabSharif101.finalProject.service.CustomerService;
import ir.maktabSharif101.finalProject.service.MainServicesService;
import ir.maktabSharif101.finalProject.service.OrderService;
import ir.maktabSharif101.finalProject.service.SubServicesService;
import ir.maktabSharif101.finalProject.service.dto.OrderSubmitDto;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;
import ir.maktabSharif101.finalProject.utils.CustomException;
import ir.maktabSharif101.finalProject.utils.Validation;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class OrderServiceImpl extends BaseEntityServiceImpl<Order,Long, OrderRepository>
implements OrderService {

    private final SubServicesService subServicesService;
    private final CustomerService customerService;
    public OrderServiceImpl(OrderRepository baseRepository,SubServicesService subServicesService,
                            CustomerService customerService) {
        super(baseRepository);
        this.subServicesService = subServicesService;
        this.customerService=customerService;
    }

    @Override
    public void submitOrder(Long customerId,OrderSubmitDto orderSubmitDto) {
        validateInfo(orderSubmitDto);

        SubServices subServices = subServicesService.findById(orderSubmitDto.getSubServiceId()).orElseThrow(() ->
                new CustomException("SubServiceNotFound", "We can't find the sub service"));
        //todo make this take Customer :/
        Customer customer = customerService.findById(customerId).orElseThrow(() ->
                new CustomException("CustomerNotFound", "We can't find you :P"));

        checkCondition(orderSubmitDto,subServices);
        Order order= mapDtoValues(orderSubmitDto);

        try {
            baseRepository.beginTransaction();
            customer.getOrders().add(order);
            subServices.getOrders().add(order);
            order.setSubServices(subServices);
            order.setCustomer(customer);
            customerService.save(customer);
            baseRepository.save(order);
            subServicesService.save(subServices);
            baseRepository.commitTransaction();
        }catch (PersistenceException e){
            baseRepository.rollbackTransaction();
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    private void validateInfo(OrderSubmitDto orderSubmitDto) {
        if (!Validation.isValidDate(orderSubmitDto.getDate())){
            throw new CustomException("InvalidDate","Correct format is (YYYY-MM-DD)");
        } else if (!Validation.isValidTime(orderSubmitDto.getTime())) {
            throw new CustomException("InvalidTime","Correct format is (HH:MM)");
        } else if (StringUtils.isBlank(orderSubmitDto.getAddress())) {
            throw new CustomException("EmptyAddress","Can't leave this field empty");
        }
    }

    protected void checkCondition(OrderSubmitDto orderSubmitDto,SubServices subServices) {
        if (orderSubmitDto.getPrice()<subServices.getBaseWage()){
            throw new CustomException("InvalidPrice","Price can't be lower than base wage");
        }
        LocalDateTime localDateTime=convertDateAndTime(orderSubmitDto.getTime(),orderSubmitDto.getDate());
        LocalDateTime now = LocalDateTime.now();
        System.out.println(localDateTime);
        System.out.println(now);
        if (localDateTime.isBefore(now)) {
            throw new CustomException("InvalidDateAndTime","Date and time can't be before now");
        }
    }

    protected Order mapDtoValues(OrderSubmitDto orderSubmitDto) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.AWAITING_TECHNICIAN_SUGGESTION);
        order.setJobInfo(orderSubmitDto.getJobInfo());
        order.setAddress(orderSubmitDto.getAddress());
        order.setPrice(orderSubmitDto.getPrice());
        order.setPoint(order.getPoint());

        //The date and time conversion
        LocalDateTime localDateTime = convertDateAndTime(orderSubmitDto.getTime(), orderSubmitDto.getDate());
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        order.setDateAndTime(date);
        return order;
    }

    private LocalDateTime convertDateAndTime(String time, String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);

        return localDate.atTime(localTime);
    }

}
