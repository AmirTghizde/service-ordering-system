package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.OrderSubmitDto;
import com.Maktab101.SpringProject.dto.OrderResponseDto;
import com.Maktab101.SpringProject.dto.SelectSuggestionDto;
import com.Maktab101.SpringProject.mapper.OrderMapper;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.service.OrderService;
import com.Maktab101.SpringProject.service.OrderSuggestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderSuggestionService orderSuggestionService;

    @Autowired
    public OrderController(OrderService orderService, OrderSuggestionService orderSuggestionService) {
        this.orderService = orderService;
        this.orderSuggestionService = orderSuggestionService;
    }

    @PostMapping("/{customerId}/submit")
    public ResponseEntity<Void> submitOrder(
            @PathVariable(name = "customerId") Long customerId,
            @Valid @RequestBody OrderSubmitDto orderSubmitDto) {
        orderService.submitOrder(customerId,orderSubmitDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/fetch/byTechnician")
    public ResponseEntity<List<OrderResponseDto>> fetchByTechnician(@RequestParam("id") Long technicianId){
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
    public ResponseEntity<Void> selectSuggestion(@Valid @RequestBody SelectSuggestionDto dto) {
        orderSuggestionService.selectSuggestion(dto.getOrderId(), dto.getSuggestionId());
        return ResponseEntity.ok().build();
    }
    @PutMapping("/start")
    public ResponseEntity<Void> startOrder(@RequestParam("id") Long orderId) {
        orderService.startOrder(orderId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/finish")
    public ResponseEntity<Void> finishOrder(@RequestParam("id") Long orderId) {
        orderService.finishOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
