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

    @PostMapping
    public ResponseEntity<TechnicianResponseDto> registerTechnician(@Valid @RequestBody RegisterDto registerDto) {
        Technician technician = technicianService.register(registerDto);
        TechnicianResponseDto technicianDto = UserMapper.INSTANCE.toTechnicianDto(technician);
        return ResponseEntity.status(HttpStatus.CREATED).body(technicianDto);
    }
    @GetMapping("/sort")
    public ResponseEntity<List<TechnicianResponseDto>> sortCustomers(@RequestBody List<String> sortingFields) {
        List<Technician> sortedTechnician = technicianService.sort(sortingFields);
        List<TechnicianResponseDto> dtoList = sortedTechnician.stream()
                .map(UserMapper.INSTANCE::toTechnicianDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TechnicianResponseDto>> sortCustomers(@RequestBody RequestDto requestDto) {
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
    public ResponseEntity<List<TechnicianResponseDto>> fetchAll() {
        List<Technician> technicians = technicianService.findAll();
        List<TechnicianResponseDto> dtoList = technicians.stream()
                .map(UserMapper.INSTANCE::toTechnicianDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(dtoList);
    }

    @PutMapping("/confirm")
    public ResponseEntity<Void> confirmTechnician(@RequestParam("id") Long technicianId) {
        technicianService.confirmTechnician(technicianId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/addImage")
    public ResponseEntity<Void> saveImage(@Valid @RequestBody ImageSaveDto dto) {
        String imagePath = "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\";
        technicianService.saveImage(dto.getTechnicianId(), imagePath + dto.getImageName());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/password")
    public ResponseEntity<Void> editPassword(@Valid @RequestBody PasswordEditDto dto) {
        technicianService.editPassword(dto.getUserId(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
