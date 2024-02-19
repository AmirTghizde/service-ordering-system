package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.services.SubServiceSubmitDto;
import com.Maktab101.SpringProject.dto.services.SubServiceTechnicianDto;
import com.Maktab101.SpringProject.dto.services.SubServicesResponseDto;
import com.Maktab101.SpringProject.mapper.SubServicesMapper;
import com.Maktab101.SpringProject.model.SubServices;
import com.Maktab101.SpringProject.service.SubServicesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subServices")
public class SubServiceController {
    private final SubServicesService subServicesService;

    @Autowired
    public SubServiceController(SubServicesService subServicesService) {
        this.subServicesService = subServicesService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> addService(@Valid @RequestBody SubServiceSubmitDto subServiceDto) {
        subServicesService.addService(
                subServiceDto.getName(),
                subServiceDto.getBaseWage(),
                subServiceDto.getDescription(),
                subServiceDto.getMainServiceName()
        );
        return ResponseEntity.ok("🔧 New subService created");
    }

    @GetMapping(path = "/view/service")
    @PreAuthorize("hasAnyRole('MANAGER','CUSTOMER','TECHNICIAN')")
    public ResponseEntity<SubServicesResponseDto> fetchServiceByName(@RequestParam("serviceName") String serviceName) {
        SubServices subServices = subServicesService.findByName(serviceName);
        SubServicesResponseDto dto = SubServicesMapper.INSTANCE.toDto(subServices);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(path = "/edit/baseWage")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> editBaseWage(
            @RequestParam("id") Long serviceId,
            @RequestParam("newWage") double newBaseWage
    ) {
        subServicesService.editBaseWage(serviceId, newBaseWage);
        return ResponseEntity.ok("✏ Base wage changed successfully");
    }

    @PutMapping(path = "/edit/description")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> editDescription(
            @RequestParam("id") Long serviceId,
            @RequestParam("description") String description
    ) {
        subServicesService.editDescription(serviceId, description);
        return ResponseEntity.ok("✏ Description changed successfully");
    }

    @PutMapping(path = "/edit/addTechnician")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> addTechnician(@Valid @RequestBody SubServiceTechnicianDto dto) {
        subServicesService.addToSubService(dto.getTechnicianId(), dto.getSubServiceId());
        return ResponseEntity.ok("🧰 Technician added successfully");
    }

    @PutMapping(path = "/edit/deleteTechnician")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteTechnician(@RequestBody SubServiceTechnicianDto dto) {
        subServicesService.deleteFromSubService(dto.getTechnicianId(), dto.getSubServiceId());
        return ResponseEntity.ok("🧰 Technician deleted successfully");
    }
}
