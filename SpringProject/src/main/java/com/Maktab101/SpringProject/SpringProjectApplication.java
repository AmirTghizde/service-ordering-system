package com.Maktab101.SpringProject;


import com.Maktab101.SpringProject.dto.RegisterDto;
import com.Maktab101.SpringProject.model.Manager;
import com.Maktab101.SpringProject.service.ManagerService;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
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
        if (!managerService.existsByEmailAddress(email)) {
            managerService.register(new RegisterDto("Admin", "Admin", email, password));
        }
    }
}
