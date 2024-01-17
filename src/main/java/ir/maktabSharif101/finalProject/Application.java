package ir.maktabSharif101.finalProject;
import ir.maktabSharif101.finalProject.service.*;
import ir.maktabSharif101.finalProject.service.dto.OrderSubmitDto;
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
//                        System.out.println(subServices.getName()));

//        mainServicesService.findAll().stream().forEach(mainService -> System.out.println(mainService.getName()));
//
//        subServiceService.addService("CompanyCleaning",500,"blah blah blah",
//                "Cleaning");

//        subServiceService.findAll().stream().forEach(subServices -> System.out.println(subServices.getName()));

//        SubServices subServices = subServiceService.findByName("CompanyCleaning").orElse(null);
//        subServices.getTechnicians().forEach(technician ->
//                System.out.println(technician.getFirstname()+" "+technician.getLastname() ));

//            Customer customer = customerService.register(new RegisterDto(
//                    "Ali", "Alavi", "Aldwi@gamil.com", "Ali1234"
//            ));

//        technicianService.register(
//                new RegisterDto(
//                        "Morteza","Alavi","Mori@gmail.com","Mori1234"
//                ), "D:\\Java\\Maktab\\HW\\HwFinal\\src\\main\\resources\\images\\Untitled2.jpg");

//        technicianService.editPassword(3L,"@Alireza1234");

//        technicianService.confirmTechnician(3L);
//
//        subServiceService.addToSubService(3L,2L);

//        subServiceService.deleteFromSubService(3L,2L);

//        managerService.register(new RegisterDto(
//                "GholamReza","Alavi","Admin@gmail.com","Admin1234"
//        ));

//        orderService.submitOrder(7L,new OrderSubmitDto(
//2L,"Clean home get money🤯","2024-01-20","12:00","Hamin baghala",15000
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
        //todo add menu
        //todo add logs

    }
}


