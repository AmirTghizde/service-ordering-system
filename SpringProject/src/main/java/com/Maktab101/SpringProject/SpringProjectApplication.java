package com.Maktab101.SpringProject;


import com.Maktab101.SpringProject.dto.users.RegisterDto;
import com.Maktab101.SpringProject.service.ManagerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@SuppressWarnings("unused")
public class SpringProjectApplication {
    static String email = "Admin@gmail.com";
    static String password = "Admin1234";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringProjectApplication.class, args);
        ManagerService managerService = context.getBean(ManagerService.class);
        BCryptPasswordEncoder passwordEncoder = context.getBean(BCryptPasswordEncoder.class);
        if (!managerService.existsByEmailAddress(email)) {
            managerService.register(new RegisterDto("Admin", "Admin", email, password));
        }
        System.out.println(
                passwordEncoder.encode("Ali123")
        );

    }
}
