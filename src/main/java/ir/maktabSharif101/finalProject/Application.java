package ir.maktabSharif101.finalProject;

import ir.maktabSharif101.finalProject.service.*;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;
import ir.maktabSharif101.finalProject.utils.ApplicationContext;
import ir.maktabSharif101.finalProject.utils.CustomException;

public class Application {
    public static void main(String[] args) {
        MainServicesService mainServicesService = ApplicationContext.getMainServiceService();
        SubServicesService subServiceService = ApplicationContext.getSubServiceService();
        CustomerService customerService = ApplicationContext.getCustomerService();
        TechnicianService technicianService = ApplicationContext.getTechnicianService();
        ManagerService managerService = ApplicationContext.getManagerService();
        OrderService orderService = ApplicationContext.getOrderService();

        try {
//        mainServicesService.addService("Cleaning");

//        MainServices mainServices = mainServicesService.findByName("Cleaning").orElse(null);
//        mainServices.getSubServices().stream().forEach(subServices->
//                        System.out.println(subServices.getName())
//                );
//
//        subServiceService.addService("CompanyCleaning",500,"blah blah blah",
//                "Cleaning");

//        subServiceService.findAll().stream().forEach(subServices -> System.out.println(subServices.getName()));

            customerService.register(new RegisterDto(
                    "Ali", "Alavi", "Ali@gamil.com", "Ali1234"
            ));
//        technicianService.register(
//                new RegisterDto(
//                        "Alireza","Alavi","Alireza@gmail.com","@Ali1234"
//                ), "D:\\Java\\Maktab\\HW\\HwFinal\\src\\main\\resources\\images\\Untitled3.jpg");

//        technicianService.editPassword(1L,"@Alireza1234");
//
//        subServiceService.addTechnician(3L,"HomeCleaning");
//        subServiceService.deleteTechnician(3L,"HouseCleaning");

//        managerService.register(new RegisterDto(
//                "GholamReza","Alavi","Admin@gmail.com","#Admin1234"
//        ));

//        technicianService.confirmTechnician(5L);


//        orderService.submitOrder(3L,new OrderSubmitDto(
//2L,"Clean home get money🤯","2024-01-17","12:00","Hamin baghala",15000
//        ));
        }catch (CustomException e){
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
        //todo make it dumb proof
        //todo add menu
        //todo add logs

    }
}


