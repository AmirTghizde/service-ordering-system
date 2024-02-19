package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.order.*;
import com.Maktab101.SpringProject.dto.suggestion.SelectSuggestionDto;
import com.Maktab101.SpringProject.dto.users.CardPaymentDto;
import com.Maktab101.SpringProject.dto.users.CustomerResponseDto;
import com.Maktab101.SpringProject.dto.users.RequestDto;
import com.Maktab101.SpringProject.mapper.OrderMapper;
import com.Maktab101.SpringProject.mapper.UserMapper;
import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.service.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin("http://localhost:3000")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final TechnicianService technicianService;
    private final SuggestionService suggestionService;
    private final OrderSuggestionService orderSuggestionService;
    private final FilterSpecification<Order> filterSpecification;
    int numberCaptcha = 0;

    @Autowired
    public OrderController(OrderService orderService, CustomerService customerService,
                           TechnicianService technicianService, SuggestionService suggestionService,
                           OrderSuggestionService orderSuggestionService, FilterSpecification<Order> filterSpecification) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.technicianService = technicianService;
        this.suggestionService = suggestionService;
        this.orderSuggestionService = orderSuggestionService;
        this.filterSpecification = filterSpecification;
    }

    @PostMapping("/{customerId}/submit")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<String> submitOrder(
            @PathVariable(name = "customerId") Long customerId,
            @Valid @RequestBody OrderSubmitDto orderSubmitDto) {
        orderService.submitOrder(customerId, orderSubmitDto);
        return ResponseEntity.ok("📜 New order submitted");
    }

    @GetMapping("/fetch/byTechnician")
    @PreAuthorize("hasAnyRole('TECHNICIAN','MANAGER')")
    public ResponseEntity<List<OrderResponseDto>> fetchByTechnician(@RequestParam("id") Long technicianId) {
        List<Order> orders = orderService.findAwaitingOrdersByTechnician(technicianId);
        List<OrderResponseDto> dtoList = orders.stream()
                .map(OrderMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/fetch/byId")
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER','TECHNICIAN')")
    public ResponseEntity<OrderResponseDto> fetchById(@RequestParam("id") Long orderId) {
        Order order = orderService.findById(orderId);
        OrderResponseDto dto = OrderMapper.INSTANCE.toDto(order);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/suggestions/select")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<String> selectSuggestion(@Valid @RequestBody SelectSuggestionDto dto) {
        orderSuggestionService.selectSuggestion(dto.getOrderId(), dto.getSuggestionId());
        return ResponseEntity.ok("✅ Suggestion successfully selected");
    }

    @PutMapping("/start")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<String> startOrder(@RequestParam("id") Long orderId) {
        orderService.startOrder(orderId);
        return ResponseEntity.ok("👨‍🔧 Order started successfully");
    }

    @PutMapping("/finish")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<String> finishOrder(@Valid @RequestBody FinishOrderDto dto) {
        orderSuggestionService.handelFinishOrder(dto);
        return ResponseEntity.ok("👷‍♂️ Order finished successfully");
    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<OrderResponseDto>> filter(@Valid @RequestBody RequestDto requestDto) {
        Specification<Order> specificationList = filterSpecification.getSpecificationList(
                requestDto.getSearchRequestDto(),
                requestDto.getGlobalOperator());

        List<Order> orderList = orderService.filter(specificationList);

        List<OrderResponseDto> dtoList = orderList.stream()
                .map(OrderMapper.INSTANCE::toDto)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/payment/byCredit")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<String> payByCredit(@RequestParam("id") Long orderId) {
        Order order = orderService.findById(orderId);
        Suggestion suggestion = order.getSelectedSuggestion();

        customerService.payByCredit(order.getCustomer().getId(), order.getPrice());
        technicianService.addCredit(suggestion.getTechnician().getId(), order.getPrice());
        order.setOrderStatus(OrderStatus.PAID);
        orderService.save(order);
        return ResponseEntity.ok("🤝 Payment completed");
    }

    @CrossOrigin
    @PutMapping("/payment/onlinePayment")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<String> payOnline(@Valid @RequestBody CardPaymentDto dto) {
        log.info("Paying online with this data [{}]", dto);
        orderSuggestionService.payOnline(dto, numberCaptcha);
        return ResponseEntity.ok("🤝 Payment completed");
    }

    @CrossOrigin
    @GetMapping("/payment")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<RequestOrderDto> fetchOrderData(@RequestParam("id") Long orderId) {
        Order order = orderService.findById(orderId);
        numberCaptcha = orderService.getNumberCaptcha();
        RequestOrderDto requestOrderDto = new RequestOrderDto();
        requestOrderDto.setAmount(order.getPrice());
        requestOrderDto.setOrderId(orderId);
        requestOrderDto.setCaptcha(numberCaptcha);
        log.info("Sending [{}]", requestOrderDto);
        return ResponseEntity.ok(requestOrderDto);
    }

    @CrossOrigin
    @GetMapping("/payment/getCaptcha")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<Integer> fetchCaptcha() {
        numberCaptcha = orderService.getNumberCaptcha();
        return ResponseEntity.ok(numberCaptcha);
    }

    @GetMapping("/view")
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER','TECHNICIAN')")
    public ResponseEntity<OrderCommentDto> viewComments(@RequestParam("id") Long orderId) {
        Order order = orderService.findById(orderId);
        OrderCommentDto orderCommentDto = OrderMapper.INSTANCE.toOrderCommentDto(order);
        return ResponseEntity.ok(orderCommentDto);
    }
}
