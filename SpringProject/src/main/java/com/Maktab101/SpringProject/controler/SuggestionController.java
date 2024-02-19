package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.suggestion.SendSuggestionDto;
import com.Maktab101.SpringProject.dto.suggestion.SuggestionResponseDto;
import com.Maktab101.SpringProject.mapper.SuggestionMapper;
import com.Maktab101.SpringProject.model.Suggestion;
import com.Maktab101.SpringProject.service.OrderSuggestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('TECHNICIAN')")
    public ResponseEntity<String> sendSuggestion(
            @PathVariable(name = "technicianId") Long technicianId,
            @Valid @RequestBody SendSuggestionDto suggestionDto) {
        orderSuggestionService.sendSuggestion(technicianId, suggestionDto);
        return ResponseEntity.ok("📮 Suggestion sent successfully");
    }

    @GetMapping("/fetch/byPoint")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<List<SuggestionResponseDto>> fetchByPoint(@RequestParam("id") Long orderId) {
        List<Suggestion> suggestions =
                orderSuggestionService.getSuggestionByTechnicianPoint(orderId, false);

        List<SuggestionResponseDto> dtoList = suggestions.stream()
                .map(SuggestionMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/fetch/byPrice")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    public ResponseEntity<List<SuggestionResponseDto>> fetchByPrice(@RequestParam("id") Long orderId) {
        List<Suggestion> suggestions =
                orderSuggestionService.getSuggestionByPrice(orderId, false);

        List<SuggestionResponseDto> dtoList = suggestions.stream()
                .map(SuggestionMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(dtoList);
    }
}
