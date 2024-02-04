package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.RegisterDto;
import com.Maktab101.SpringProject.service.CustomerService;
import com.Maktab101.SpringProject.service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping
    public ResponseEntity<Void> registerManager(@Valid @RequestBody RegisterDto registerDto) {
        managerService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
