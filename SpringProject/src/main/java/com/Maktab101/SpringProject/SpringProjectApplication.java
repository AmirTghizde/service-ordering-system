package com.Maktab101.SpringProject;


import com.Maktab101.SpringProject.dto.users.RegisterDto;
import com.Maktab101.SpringProject.model.Manager;
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
    static String password = "Ali123";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringProjectApplication.class, args);
        ManagerService managerService = context.getBean(ManagerService.class);
        BCryptPasswordEncoder passwordEncoder = context.getBean(BCryptPasswordEncoder.class);
        if (!managerService.existsByEmailAddress(email)) {
            Manager manager = managerService.register(new RegisterDto("Admin", "Admin", email, password));
            manager.setIsEnabled(true);
            managerService.save(manager);
        }
    }
}
