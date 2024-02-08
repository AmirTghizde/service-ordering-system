package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.order.*;
import com.Maktab101.SpringProject.dto.suggestion.SelectSuggestionDto;
import com.Maktab101.SpringProject.dto.users.CardPaymentDto;
import com.Maktab101.SpringProject.mapper.OrderMapper;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.model.enums.OrderStatus;
import com.Maktab101.SpringProject.service.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    int numberCaptcha = 0;

    @Autowired
    public OrderController(OrderService orderService, CustomerService customerService,
                           TechnicianService technicianService, SuggestionService suggestionService,
                           OrderSuggestionService orderSuggestionService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.technicianService = technicianService;
        this.suggestionService = suggestionService;
        this.orderSuggestionService = orderSuggestionService;
    }

    @PostMapping("/{customerId}/submit")
    public ResponseEntity<String> submitOrder(
            @PathVariable(name = "customerId") Long customerId,
            @Valid @RequestBody OrderSubmitDto orderSubmitDto) {
        orderService.submitOrder(customerId, orderSubmitDto);
        return ResponseEntity.ok("📜 New order submitted");
    }

    @GetMapping("/fetch/byTechnician")
    public ResponseEntity<List<OrderResponseDto>> fetchByTechnician(@RequestParam("id") Long technicianId) {
        List<Order> orders = orderService.findAwaitingOrdersByTechnician(technicianId);
        List<OrderResponseDto> dtoList = orders.stream()
                .map(OrderMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/fetch/byId")
    public ResponseEntity<OrderResponseDto> fetchById(@RequestParam("id") Long orderId) {
        Order order = orderService.findById(orderId);
        OrderResponseDto dto = OrderMapper.INSTANCE.toDto(order);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/suggestions/select")
    public ResponseEntity<String> selectSuggestion(@Valid @RequestBody SelectSuggestionDto dto) {
        orderSuggestionService.selectSuggestion(dto.getOrderId(), dto.getSuggestionId());
        return ResponseEntity.ok("✅ Suggestion successfully selected");
    }

    @PutMapping("/start")
    public ResponseEntity<String> startOrder(@RequestParam("id") Long orderId) {
        orderService.startOrder(orderId);
        return ResponseEntity.ok("👨‍🔧 Order started successfully");
    }

    @PutMapping("/finish")
    public ResponseEntity<String> finishOrder(@Valid @RequestBody FinishOrderDto dto) {
        orderSuggestionService.handelFinishOrder(dto);
        return ResponseEntity.ok("👷‍♂️ Order finished successfully");
    }

    @PutMapping("/payment/byCredit")
    public ResponseEntity<String> payByCredit(@RequestParam("id") Long orderId) {
        Order order = orderService.findById(orderId);
        Suggestion suggestion = suggestionService.findById(order.getSelectedSuggestionId());

        customerService.payByCredit(order.getCustomer().getId(), order.getPrice());
        technicianService.addCredit(suggestion.getTechnician().getId(), order.getPrice());
        order.setOrderStatus(OrderStatus.PAID);
        orderService.save(order);
        return ResponseEntity.ok("🤝 Payment completed");
    }

    @CrossOrigin
    @PutMapping("/payment/onlinePayment")
    public ResponseEntity<String> payOnline( @Valid @RequestBody CardPaymentDto dto) {
        orderSuggestionService.payOnline( dto, numberCaptcha);
        return ResponseEntity.ok("🤝 Payment completed");
    }

    @CrossOrigin
    @GetMapping("/payment")
    public ResponseEntity<RequestOrderDto> fetchOrderData( @RequestParam("id") Long orderId) {
        Order order = orderService.findById(orderId);
        numberCaptcha = orderService.getNumberCaptcha();
        RequestOrderDto requestOrderDto = new RequestOrderDto();
        requestOrderDto.setAmount(order.getPrice());
        requestOrderDto.setOrderId(orderId);
        requestOrderDto.setCaptcha(numberCaptcha);
        log.info("Sending [{}]",requestOrderDto);
        return ResponseEntity.ok(requestOrderDto);
    }

    @CrossOrigin
    @GetMapping("/payment/getCaptcha")
    public ResponseEntity<Integer> fetchCaptcha() {
        numberCaptcha = orderService.getNumberCaptcha();
        return ResponseEntity.ok(numberCaptcha);
    }

    @GetMapping("/view")
    public ResponseEntity<OrderCommentDto> viewOrder(@RequestParam("id") Long orderId) {
        Order order = orderService.findById(orderId);
        OrderCommentDto orderCommentDto = OrderMapper.INSTANCE.toOrderCommentDto(order);
        return ResponseEntity.ok(orderCommentDto);
    }
}
