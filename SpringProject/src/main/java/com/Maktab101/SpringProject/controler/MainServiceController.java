package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.ServiceNameDto;
import com.Maktab101.SpringProject.model.MainServices;
import com.Maktab101.SpringProject.service.MainServicesService;
import com.Maktab101.SpringProject.utils.exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mainServices")
public class MainServiceController {
    private final MainServicesService mainServicesService;

    @Autowired
    public MainServiceController(MainServicesService mainServicesService) {
        this.mainServicesService = mainServicesService;
    }

    @PostMapping
    public ResponseEntity<Void> addService(@Valid @RequestBody ServiceNameDto serviceNameDto){
        mainServicesService.addService(serviceNameDto.getServiceName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping(path = "/view/service")
    public ResponseEntity<MainServices> fetchServiceByName(@RequestParam("serviceName") String serviceName){
        MainServices mainServices = mainServicesService.findByName(serviceName);
        return ResponseEntity.ok(mainServices);
    }
    @GetMapping(path = "/view")
    public ResponseEntity<List<MainServices>> fetchAll(){
        List<MainServices> mainServices = mainServicesService.findAll();
        return ResponseEntity.ok(mainServices);
    }
    @GetMapping(path = "/view/subServices")
    public ResponseEntity<List<String>> fetchSubServices(@RequestParam("id") Long serviceId){
        List<String> subServiceNames = mainServicesService.findSubServiceNames(serviceId);
        return ResponseEntity.ok(subServiceNames);
    }

}
