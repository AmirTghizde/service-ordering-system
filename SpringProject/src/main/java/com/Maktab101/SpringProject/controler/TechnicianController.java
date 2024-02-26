package com.Maktab101.SpringProject.controler;

import com.Maktab101.SpringProject.dto.order.OrderHistoryDto;
import com.Maktab101.SpringProject.dto.users.*;
import com.Maktab101.SpringProject.mapper.OrderMapper;
import com.Maktab101.SpringProject.mapper.UserMapper;
import com.Maktab101.SpringProject.model.EmailVerification;
import com.Maktab101.SpringProject.model.Order;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.model.User;
import com.Maktab101.SpringProject.security.CurrentUser;
import com.Maktab101.SpringProject.service.EmailVerificationService;
import com.Maktab101.SpringProject.service.FilterSpecification;
import com.Maktab101.SpringProject.service.OrderService;
import com.Maktab101.SpringProject.service.TechnicianService;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.MediaPrintableArea;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;
    private final EmailVerificationService emailVerificationService;
    private final OrderService orderService;

    @Autowired
    public TechnicianController(TechnicianService technicianService, OrderService orderService, FilterSpecification<Technician> filterSpecification, EmailVerificationService emailVerificationService) {
        this.technicianService = technicianService;
        this.orderService = orderService;
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<TechnicianDataDto> registerTechnician(@Valid @RequestBody RegisterDto registerDto) {
        Technician technician = technicianService.register(registerDto);
        TechnicianDataDto technicianDto = UserMapper.INSTANCE.toTechnicianDataDto(technician);
        return ResponseEntity.status(HttpStatus.CREATED).body(technicianDto);
    }

    @GetMapping("/confirmEmail")
    @Transactional
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        if (!emailVerificationService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid token");
        }
        EmailVerification emailVerification = emailVerificationService.findByToken(token);
        technicianService.verify(emailVerification.getUser().getId(),token);

        return ResponseEntity.status(HttpStatus.OK).body("Validated Successfully you can login now");
    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<TechnicianResponseDto>> filterTechnicians(@Valid @RequestBody RequestDto requestDto) {

        List<Technician> filteredCustomers = technicianService.handelFiltering(requestDto);

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

    @PostMapping(value = "/edit/addImage", consumes = MediaType.IMAGE_JPEG_VALUE)
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<String> saveImage( @RequestBody byte[] imageData) {
        Long userId = CurrentUser.getCurrentUserId();
        if (imageData.length > 300 * 1024) {
            throw new CustomException("Max file limit is 300KB");
        }
        technicianService.saveImage(userId,imageData);
        return ResponseEntity.ok("📸 Image added successfully");
    }

    @PutMapping("/edit/password")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<String> editPassword(@Valid @RequestBody PasswordEditDto dto) {
        Long userId = CurrentUser.getCurrentUserId();
        technicianService.editPassword(userId, dto.getNewPassword());
        return ResponseEntity.ok("🔐 Password changed successfully");
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('TECHNICIAN')")
    public ResponseEntity<CurrentUserDto> viewProfile() {
        User currentUser = CurrentUser.getCurrentUser();
        CurrentUserDto currentUserDto = UserMapper.INSTANCE.toCurrentUserDto(currentUser);
        return ResponseEntity.ok(currentUserDto);
    }

    @GetMapping("/profile/image")
    @PreAuthorize("hasAnyRole('MANAGER','TECHNICIAN')")
    public ResponseEntity<byte[]>viewImage(@RequestParam("id") Long technicianId) {
        Technician technician = technicianService.findById(technicianId);

        byte[] imageData = technician.getImageData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    @GetMapping("/profile/myHistory")
    @PreAuthorize("hasAnyRole('TECHNICIAN')")
    public ResponseEntity<List<OrderHistoryDto>> viewOrderHistory(@Valid @RequestBody ViewHistoryDto dto) {

        Long userId = CurrentUser.getCurrentUserId();
        RequestDto requestDto = technicianService.getRequestDto(userId,dto.getStatus());

        List<Order> orderList = orderService.handelFiltering(requestDto);

        List<OrderHistoryDto> dtoList = orderList.stream()
                .map(OrderMapper.INSTANCE::toOrderHistoryDto)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/profile/balance")
    @PreAuthorize("hasAnyRole('MANAGER','TECHNICIAN')")
    public ResponseEntity<Double>viewBalance() {
        Long userId = CurrentUser.getCurrentUserId();
        Technician technician = technicianService.findById(userId);
        double balance = technician.getBalance();

        return ResponseEntity.ok(balance);
    }

}
