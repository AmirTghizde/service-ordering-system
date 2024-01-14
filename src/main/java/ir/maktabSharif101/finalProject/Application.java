package ir.maktabSharif101.finalProject;

import ir.maktabSharif101.finalProject.entity.Customer;
import ir.maktabSharif101.finalProject.service.CustomerService;
import ir.maktabSharif101.finalProject.service.MainServicesService;
import ir.maktabSharif101.finalProject.service.SubServicesService;
import ir.maktabSharif101.finalProject.service.TechnicianService;
import ir.maktabSharif101.finalProject.service.dto.RegisterDto;
import ir.maktabSharif101.finalProject.utils.ApplicationContext;

public class Application {
    public static void main(String[] args) {
        MainServicesService mainServicesService = ApplicationContext.getMainServiceService();
        SubServicesService subServiceService = ApplicationContext.getSubServiceService();
        CustomerService customerService = ApplicationContext.getCustomerService();
        TechnicianService technicianService = ApplicationContext.getTechnicianService();

//        mainServicesService.addService("Cleaning");

//        MainServices mainServices = mainServicesService.findByName("Cleaning").orElse(null);
//        mainServices.getSubServices().stream().forEach(subServices->
//                        System.out.println(subServices.getName())
//                );

//        subServiceService.addService("CompanyCleaning",500,"blah blah blah",
//                "Cleaning");

//        subServiceService.findAll().stream().forEach(subServices -> System.out.println(subServices.getName()));

//        customerService.register(new RegisterDto(
//                "Ali","Alavi","Ali123@gmail.com","@Ali1234"
//        ));
        technicianService.register(
                new RegisterDto(
                        "Alireza","Alavi","Alireza@gmail.com","@Ali1234"
                ), "ir/maktabSharif101/finalProject/images/Untitled.jpg");

        /*

         ____             _
        |  _ \  _____   _| |    ___   __ _
        | | | |/ _ \ \ / / |   / _ \ / _` |
        | |_| |  __/\ V /| |__| (_) | (_| |
        |____/ \___| \_/ |_____\___/ \__, |
                                     |___/
        ====================================================================================*/
        //todo finish the requested stuff
        //todo add menu
        //todo add logs

    }
}


