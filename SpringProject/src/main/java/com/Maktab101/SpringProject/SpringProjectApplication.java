package com.Maktab101.SpringProject;


import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.MainServices;
import com.Maktab101.SpringProject.service.*;
import com.Maktab101.SpringProject.service.dto.OrderSubmitDto;
import com.Maktab101.SpringProject.service.dto.RegisterDto;
import com.Maktab101.SpringProject.service.dto.SuggestionDto;
import com.Maktab101.SpringProject.utils.CustomException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringProjectApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringProjectApplication.class, args);
        ManagerService managerService = context.getBean(ManagerService.class);
        TechnicianService technicianService = context.getBean(TechnicianService.class);
        CustomerService customerService = context.getBean(CustomerService.class);
        MainServicesService mainServicesService = context.getBean(MainServicesService.class);
        SubServicesService subServicesService = context.getBean(SubServicesService.class);
        OrderService orderService = context.getBean(OrderService.class);
        SuggestionService suggestionService = context.getBean(SuggestionService.class);
        System.out.println("====================================================================================================");
        try {
//            managerService.register(new RegisterDto(
//                    "MohamadReza","Alavi","Admin@gmail.com","Admin1234"
//            ));

        mainServicesService.addService("Cleaning");

//            System.out.println(mainServicesService.findAll());

//            System.out.println(mainServicesService.findSubServiceNames(1L));
//
        subServicesService.addService("HouseCleaning",500,"blah blah blah",
                "Cleaning");

//        subServiceService.findAll().stream().forEach(subServices -> System.out.println(subServices.getName()));

//        SubServices subServices = subServiceService.findByName("CompanyCleaning").orElse(null);
//        subServices.getTechnicians().forEach(technician ->
//                System.out.println(technician.getFirstname()+" "+technician.getLastname() ));

//            subServicesService.editBaseWage(1L,1000);

//            subServicesService.editDescription(2L,"homee");

            Customer customer = customerService.register(new RegisterDto(
                    "Ali", "Alavi", "Aldwi@gmail.com", "Ali1234"
            ));

            technicianService.register(
                    new RegisterDto(
                            "Ali", "Alavi", "Ali1@gmail.com", "Ali1234"
                    ));

//        technicianService.saveImage(2L,
//                "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\Untitled1.jpg");

//        customerService.editPassword(1L,"test1234");

        technicianService.confirmTechnician(2L);
//
            subServicesService.addToSubService(2L,1L);

//        subServicesService.deleteFromSubService(2L,1L);

        orderService.submitOrder(1L,new OrderSubmitDto(
1L,"Clean home get money🤯","2025-01-20","12:00","Hamin baghala", 700
        ));

//            System.out.println(orderService.findAwaitingOrdersByTechnician(2L));

            suggestionService.sendSuggestion(2L,new SuggestionDto(
                    1L,600,"03:00","2025-01-22","00:55"
            ));

//            System.out.println(orderService.getSuggestionByPrice(1L, true));
//            System.out.println(orderService.getSuggestionByTechnicianPoint(1L, false));

        } catch (CustomException e) {
            System.out.println("********************************");
            System.out.println(e.getMessage());
            System.out.println("********************************");
        }

           /*
         ____             _
        |  _ \  _____   _| |    ___   __ _
        | | | |/ _ \ \ / / |   / _ \ / _` |
        | |_| |  __/\ V /| |__| (_) | (_| |
        |____/ \___| \_/ |_____\___/ \__, |
                                     |___/
        ====================================================================================*/
        //todo fix manger code
        //todo add logs
        //todo use builder...?
    }
}
