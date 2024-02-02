package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.service.MainServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/MainService")
public class MainServiceController {
    private final MainServicesService mainServicesService;

    @Autowired
    public MainServiceController(MainServicesService mainServicesService) {
        this.mainServicesService = mainServicesService;
    }

    @PostMapping
    public ResponseEntity<Void> addService(@RequestBody String serviceName){
        mainServicesService.addService(serviceName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
