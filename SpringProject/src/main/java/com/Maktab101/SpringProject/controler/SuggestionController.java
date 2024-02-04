package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.*;
import com.Maktab101.SpringProject.dto.services.MainServicesResponseDto;
import com.Maktab101.SpringProject.mapper.MainServicesMapper;
import com.Maktab101.SpringProject.mapper.OrderMapper;
import com.Maktab101.SpringProject.mapper.SuggestionMapper;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.service.OrderService;
import com.Maktab101.SpringProject.service.OrderSuggestionService;
import com.Maktab101.SpringProject.service.SuggestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/suggestions")
public class SuggestionController {

    private final OrderSuggestionService orderSuggestionService;

    @Autowired
    public SuggestionController(OrderSuggestionService orderSuggestionService) {
        this.orderSuggestionService = orderSuggestionService;
    }

    @PostMapping("/{technicianId}/send")
    public ResponseEntity<Void> sendSuggestion(
            @PathVariable(name = "technicianId") Long technicianId,
            @Valid @RequestBody SendSuggestionDto suggestionDto) {
        orderSuggestionService.sendSuggestion(technicianId, suggestionDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/fetch/byPoint")
    public ResponseEntity<List<SuggestionResponseDto>> fetchByPoint(@RequestParam("id") Long orderId) {
        List<Suggestion> suggestions =
                orderSuggestionService.getSuggestionByTechnicianPoint(orderId, false);

        List<SuggestionResponseDto> dtoList = suggestions.stream()
                .map(SuggestionMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/fetch/byPrice")
    public ResponseEntity<List<SuggestionResponseDto>> fetchByPrice(@RequestParam("id") Long orderId) {
        List<Suggestion> suggestions =
                orderSuggestionService.getSuggestionByPrice(orderId, false);

        List<SuggestionResponseDto> dtoList = suggestions.stream()
                .map(SuggestionMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(dtoList);
    }
}
