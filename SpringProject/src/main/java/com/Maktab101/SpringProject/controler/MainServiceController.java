package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.services.MainServicesResponseDto;
import com.Maktab101.SpringProject.dto.services.ServiceNameDto;
import com.Maktab101.SpringProject.mapper.MainServicesMapper;
import com.Maktab101.SpringProject.model.MainServices;
import com.Maktab101.SpringProject.service.MainServicesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mainServices")
public class MainServiceController {
    private final MainServicesService mainServicesService;


    @Autowired
    public MainServiceController(MainServicesService mainServicesService) {
        this.mainServicesService = mainServicesService;
    }

    @PostMapping
    public ResponseEntity<String> addService(@Valid @RequestBody ServiceNameDto serviceNameDto) {
        mainServicesService.addService(serviceNameDto.getServiceName());
        return ResponseEntity.status(HttpStatus.CREATED).body("🔨 New main service created");
    }

    @GetMapping(path = "/view/service")
    public ResponseEntity<MainServicesResponseDto> fetchServiceByName(@RequestParam("serviceName") String serviceName) {
        MainServices mainServices = mainServicesService.findByName(serviceName);

        MainServicesResponseDto dto = MainServicesMapper.INSTANCE.toDto(mainServices);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(path = "/view")
    public ResponseEntity<List<MainServicesResponseDto>> fetchAll() {
        List<MainServices> mainServices = mainServicesService.findAll();

        List<MainServicesResponseDto> responseDtoList = mainServices.stream()
                .map(MainServicesMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping(path = "/view/subServices")
    public ResponseEntity<List<String>> fetchSubServices(@RequestParam("id") Long serviceId) {
        List<String> subServiceNames = mainServicesService.findSubServiceNames(serviceId);
        return ResponseEntity.ok(subServiceNames);
    }

}
