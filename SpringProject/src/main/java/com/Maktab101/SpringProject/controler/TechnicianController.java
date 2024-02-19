package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.users.*;
import com.Maktab101.SpringProject.mapper.UserMapper;
import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.service.FilterSpecification;
import com.Maktab101.SpringProject.service.TechnicianService;
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
@RequestMapping("/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    private final FilterSpecification<Technician> filterSpecification;

    @Autowired
    public TechnicianController(TechnicianService technicianService, FilterSpecification<Technician> filterSpecification) {
        this.technicianService = technicianService;
        this.filterSpecification = filterSpecification;
    }

    @PostMapping("/register")
    public ResponseEntity<TechnicianResponseDto> registerTechnician(@Valid @RequestBody RegisterDto registerDto) {
        Technician technician = technicianService.register(registerDto);
        TechnicianResponseDto technicianDto = UserMapper.INSTANCE.toTechnicianDto(technician);
        return ResponseEntity.status(HttpStatus.CREATED).body(technicianDto);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<TechnicianResponseDto>> filterTechnicians(@Valid @RequestBody RequestDto requestDto) {
        Specification<Technician> specificationList = filterSpecification.getSpecificationList(
                requestDto.getSearchRequestDto(),
                requestDto.getGlobalOperator());

        List<Technician> filteredCustomers = technicianService.filter(specificationList);

        List<TechnicianResponseDto> dtoList = filteredCustomers.stream()
                .map(UserMapper.INSTANCE::toTechnicianDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);

    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<TechnicianResponseDto>> fetchAll() {
        List<Technician> technicians = technicianService.findAll();
        List<TechnicianResponseDto> dtoList = technicians.stream()
                .map(UserMapper.INSTANCE::toTechnicianDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(dtoList);
    }

    @PutMapping("/confirm")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> confirmTechnician(@RequestParam("id") Long technicianId) {
        technicianService.confirmTechnician(technicianId);
        return ResponseEntity.ok("✅ Suggestion confirmed successfully");
    }

    @PutMapping("/edit/addImage")
    @PreAuthorize("hasAnyRole('MANAGER','TECHNICIAN')")
    public ResponseEntity<String> saveImage(@Valid @RequestBody ImageSaveDto dto) {
        String imagePath = "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\";
        technicianService.saveImage(dto.getTechnicianId(), imagePath + dto.getImageName());
        return ResponseEntity.ok("📸 Image added successfully");
    }

    @PutMapping("/edit/password")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<String> editPassword(@Valid @RequestBody PasswordEditDto dto) {
        technicianService.editPassword(dto.getUserId(), dto.getNewPassword());
        return ResponseEntity.ok("🔐 Password changed successfully");
    }

    @GetMapping("/balance")
    @PreAuthorize("hasAnyRole('MANAGER','TECHNICIAN')")
    public ResponseEntity<Double>viewBalance(@RequestParam("id") Long customerId) {
        Technician technician = technicianService.findById(customerId);
        double balance = technician.getBalance();

        return ResponseEntity.ok(balance);
    }
}
