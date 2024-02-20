package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.order.OrderHistoryDto;
import com.Maktab101.SpringProject.dto.users.*;
import com.Maktab101.SpringProject.mapper.OrderMapper;
import com.Maktab101.SpringProject.mapper.UserMapper;
import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.service.CustomerService;
import com.Maktab101.SpringProject.service.FilterSpecification;
import com.Maktab101.SpringProject.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;
    private final FilterSpecification<Customer> filterSpecification;
    private final FilterSpecification<Order> orderFilterSpecification;

    @Autowired
    public CustomerController(CustomerService customerService,
                              FilterSpecification<Customer> filterSpecification1, OrderService orderService, FilterSpecification<Order> orderFilterSpecification) {
        this.customerService = customerService;
        this.filterSpecification = filterSpecification1;
        this.orderService = orderService;
        this.orderFilterSpecification = orderFilterSpecification;
    }


    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDto> registerCustomer(@Valid @RequestBody RegisterDto registerDto) {
        Customer customer = customerService.register(registerDto);
        CustomerResponseDto customerDto = UserMapper.INSTANCE.toCustomerDto(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
    }

    @PutMapping("/edit/password")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<String> editPassword(@Valid @RequestBody PasswordEditDto dto) {
        customerService.editPassword(dto.getUserId(), dto.getNewPassword());
        return ResponseEntity.ok("🔐 Password changed successfully");
    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<CustomerResponseDto>> filterCustomers(@Valid @RequestBody RequestDto requestDto) {
        Specification<Customer> specificationList = filterSpecification.getSpecificationList(
                requestDto.getSearchRequestDto(),
                requestDto.getGlobalOperator());

        List<Customer> filteredCustomers = customerService.filter(specificationList);

        List<CustomerResponseDto> dtoList = filteredCustomers.stream()
                .map(UserMapper.INSTANCE::toCustomerDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/credit/add")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<String> addCredit(@Valid @RequestBody AddCreditDto dto) {
        customerService.addCredit(dto.getCustomerId(), dto.getAmount());
        return ResponseEntity.ok("💳 Credit increased successfully");
    }

    @GetMapping("/profile/balance")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<Double> viewBalance(@RequestParam("id") Long customerId) {
        Customer customer = customerService.findById(customerId);
        double balance = customer.getBalance();

        return ResponseEntity.ok(balance);
    }

    @GetMapping("/profile/myHistory")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<List<OrderHistoryDto>> viewOrderHistory(@Valid @RequestBody ViewHistoryDto dto) {

        RequestDto requestDto = customerService.getRequestDto(dto.getId(),dto.getStatus());

        Specification<Order> specificationList = orderFilterSpecification.getSpecificationList(
                requestDto.getSearchRequestDto(),
                requestDto.getGlobalOperator());

        List<Order> orderList = orderService.filter(specificationList);

        List<OrderHistoryDto> dtoList = orderList.stream()
                .map(OrderMapper.INSTANCE::toOrderHistoryDto)
                .toList();


        return ResponseEntity.ok(dtoList);
    }
}
