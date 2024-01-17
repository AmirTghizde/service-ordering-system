package ir.maktabSharif101.finalProject.utils;

import ir.maktabSharif101.finalProject.repository.*;
import ir.maktabSharif101.finalProject.repository.impl.*;
import ir.maktabSharif101.finalProject.service.*;
import ir.maktabSharif101.finalProject.service.impl.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class ApplicationContext {
    public static final EntityManager ENTITY_MANAGER =
            Persistence.createEntityManagerFactory(
                    "default"
            ).createEntityManager();
    static ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();
    public static final Validator VALIDATOR = factory.getValidator();

    private static CustomerRepository customerRepository;
    private static MainServicesRepository mainServicesRepository;
    private static ManagerRepository managerRepository;
    private static OrderRepository orderRepository;
    private static SubServicesRepository subServicesRepository;
    private static SuggestionRepository suggestionRepository;
    private static TechnicianRepository technicianRepository;


    private static CustomerService customerService;
    private static MainServicesService mainServicesService;
    private static ManagerService managerService;
    private static OrderService orderService;
    private static SubServicesService subServicesService;
    private static SuggestionService suggestionService;
    private static TechnicianService technicianService;


    public static CustomerRepository getCustomerRepository() {
        if (customerRepository == null) {
            customerRepository = new CustomerRepositoryImpl(ENTITY_MANAGER);
        }
        return customerRepository;
    }

    public static MainServicesRepository getMainServiceRepository() {
        if (mainServicesRepository == null) {
            mainServicesRepository = new MainServicesRepositoryImpl(ENTITY_MANAGER);
        }
        return mainServicesRepository;
    }

    public static ManagerRepository getManagerRepository() {
        if (managerRepository == null) {
            managerRepository = new ManagerRepositoryImpl(ENTITY_MANAGER);
        }
        return managerRepository;
    }

    public static OrderRepository getOrderRepository() {
        if (orderRepository == null) {
            orderRepository = new OrderRepositoryImpl(ENTITY_MANAGER);
        }
        return orderRepository;
    }

    public static SubServicesRepository getSubServiceRepository() {
        if (subServicesRepository == null) {
            subServicesRepository = new SubServicesRepositoryImpl(ENTITY_MANAGER);
        }
        return subServicesRepository;
    }

    public static SuggestionRepository getSuggestionRepository() {
        if (suggestionRepository == null) {
            suggestionRepository = new SuggestionRepositoryImpl(ENTITY_MANAGER);
        }
        return suggestionRepository;
    }

    public static TechnicianRepository getTechnicianRepository() {
        if (technicianRepository == null) {
            technicianRepository = new TechnicianRepositoryImpl(ENTITY_MANAGER);
        }
        return technicianRepository;
    }

    public static CustomerService getCustomerService() {
        if (customerService == null) {
            customerService = new CustomerServiceImpl(
                    getCustomerRepository(),
                    VALIDATOR
            );
        }
        return customerService;
    }

    public static MainServicesService getMainServiceService() {
        if (mainServicesService == null) {
            mainServicesService = new MainServicesServiceImpl(getMainServiceRepository());
        }
        return mainServicesService;
    }

    public static ManagerService getManagerService() {
        if (managerService == null) {
            managerService = new ManagerServiceImpl(
                    getManagerRepository(),
                    VALIDATOR
            );
        }
        return managerService;
    }

    public static OrderService getOrderService() {
        if (orderService == null) {
            orderService = new OrderServiceImpl(
                    getOrderRepository(),
                    getSubServiceService(),
                    getCustomerService(),
                    VALIDATOR
            );
        }
        return orderService;
    }

    public static SubServicesService getSubServiceService() {
        if (subServicesService == null) {
            subServicesService = new SubServicesServiceImpl(
                    getSubServiceRepository(),
                    getMainServiceService(),
                    getTechnicianService()
            );
        }
        return subServicesService;
    }

    public static SuggestionService getSuggestionService() {
        if (suggestionService == null) {
            suggestionService = new SuggestionServiceImpl(getSuggestionRepository());
        }
        return suggestionService;
    }

    public static TechnicianService getTechnicianService() {
        if (technicianService == null) {
            technicianService = new TechnicianServiceImpl(
                    getTechnicianRepository(),
                    VALIDATOR
            );
        }
        return technicianService;
    }
}
