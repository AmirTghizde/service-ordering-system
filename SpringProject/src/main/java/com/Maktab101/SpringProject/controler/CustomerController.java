package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.order.OrderHistoryDto;
import com.Maktab101.SpringProject.dto.users.*;
import com.Maktab101.SpringProject.mapper.OrderMapper;
import com.Maktab101.SpringProject.mapper.UserMapper;
import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.EmailVerification;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.service.CustomerService;
import com.Maktab101.SpringProject.service.EmailVerificationService;
import com.Maktab101.SpringProject.service.OrderService;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;
    private final EmailVerificationService emailVerificationService;


    @Autowired
    public CustomerController(CustomerService customerService, OrderService orderService, EmailVerificationService emailVerificationService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.emailVerificationService = emailVerificationService;
    }


    @PostMapping("/register")
    public ResponseEntity<CustomerDataDto> registerCustomer(@Valid @RequestBody RegisterDto registerDto) {
        Customer customer = customerService.register(registerDto);
        CustomerDataDto customerDto = UserMapper.INSTANCE.toCustomerDataDto(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
    }

    @GetMapping("/confirmEmail")
    @Transactional
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        if (!emailVerificationService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid token");
        }
        EmailVerification emailVerification = emailVerificationService.findByToken(token);
        customerService.verify(emailVerification.getUser().getId(),token);

        return ResponseEntity.status(HttpStatus.OK).body("Validated Successfully you can login now");
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

        List<Customer> filteredCustomers = customerService.handelFiltering(requestDto);

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

        List<Order> orderList = orderService.handelFiltering(requestDto);

        List<OrderHistoryDto> dtoList = orderList.stream()
                .map(OrderMapper.INSTANCE::toOrderHistoryDto)
                .toList();


        return ResponseEntity.ok(dtoList);
    }
}
