package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.ImageSaveDto;
import com.Maktab101.SpringProject.dto.RegisterDto;
import com.Maktab101.SpringProject.dto.TechnicianResponseDto;
import com.Maktab101.SpringProject.mapper.UserMapper;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.service.ManagerService;
import com.Maktab101.SpringProject.service.TechnicianService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    @Autowired
    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @PostMapping
    public ResponseEntity<TechnicianResponseDto> registerTechnician(@Valid @RequestBody RegisterDto registerDto) {
        Technician technician = technicianService.register(registerDto);
        TechnicianResponseDto technicianDto = UserMapper.INSTANCE.toTechnicianDto(technician);
        return ResponseEntity.status(HttpStatus.CREATED).body(technicianDto);
    }

    @PutMapping("/confirm")
    public ResponseEntity<Void> confirmTechnician(@RequestParam("id") Long technicianId) {
        technicianService.confirmTechnician(technicianId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/addImage")
    public ResponseEntity<Void> saveImage(@Valid @RequestBody ImageSaveDto dto) {
        String imagePath = "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\";
        technicianService.saveImage(dto.getTechnicianId(), imagePath+dto.getImageName());
        return ResponseEntity.ok().build();
    }
}
