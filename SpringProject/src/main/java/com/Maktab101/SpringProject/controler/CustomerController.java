package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.order.OrderHistoryDto;
import com.Maktab101.SpringProject.dto.order.OrderResponseDto;
import com.Maktab101.SpringProject.dto.users.*;
import com.Maktab101.SpringProject.mapper.OrderMapper;
import com.Maktab101.SpringProject.mapper.UserMapper;
import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.service.CustomerService;
import com.Maktab101.SpringProject.service.FilterSpecification;
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
    private final FilterSpecification<Customer> filterSpecification;

    @Autowired
    public CustomerController(CustomerService customerService,
                              FilterSpecification<Customer> filterSpecification1) {
        this.customerService = customerService;
        this.filterSpecification = filterSpecification1;
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

    @GetMapping("/balance")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<Double> viewBalance(@RequestParam("id") Long customerId) {
        Customer customer = customerService.findById(customerId);
        double balance = customer.getBalance();

        return ResponseEntity.ok(balance);
    }
}
