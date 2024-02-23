package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.users.ManagerResponseDto;
import com.Maktab101.SpringProject.dto.users.PasswordEditDto;
import com.Maktab101.SpringProject.dto.users.RegisterDto;
import com.Maktab101.SpringProject.mapper.UserMapper;
import com.Maktab101.SpringProject.model.EmailVerification;
import com.Maktab101.SpringProject.model.Manager;
import com.Maktab101.SpringProject.service.EmailVerificationService;
import com.Maktab101.SpringProject.service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public ManagerController(ManagerService managerService, EmailVerificationService emailVerificationService) {
        this.managerService = managerService;
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<ManagerResponseDto> registerManager(@Valid @RequestBody RegisterDto registerDto) {
        Manager manager = managerService.register(registerDto);
        ManagerResponseDto managerDto = UserMapper.INSTANCE.toManagerDto(manager);
        return ResponseEntity.status(HttpStatus.CREATED).body(managerDto);
    }

    @GetMapping("/confirmEmail")
    @Transactional
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        if (!emailVerificationService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid token");
        }
        EmailVerification emailVerification = emailVerificationService.findByToken(token);
        managerService.verify(emailVerification.getUser().getId(),token);

        return ResponseEntity.status(HttpStatus.OK).body("Validated Successfully you can login now");
    }

    @PutMapping("/edit/password")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> editPassword(@Valid @RequestBody PasswordEditDto dto) {
        managerService.editPassword(dto.getUserId(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
