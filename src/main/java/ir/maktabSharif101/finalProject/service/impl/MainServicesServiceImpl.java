package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.base.service.BaseEntityServiceImpl;
import ir.maktabSharif101.finalProject.entity.MainServices;
import ir.maktabSharif101.finalProject.repository.MainServicesRepository;
import ir.maktabSharif101.finalProject.service.MainServicesService;
import ir.maktabSharif101.finalProject.utils.CustomException;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Optional;

public class MainServicesServiceImpl extends BaseEntityServiceImpl<MainServices, Long, MainServicesRepository>
        implements MainServicesService {
    public MainServicesServiceImpl(MainServicesRepository baseRepository) {
        super(baseRepository);
    }

    @Override
    public void addService(String serviceName) {
        checkConditions(serviceName);
        MainServices mainServices = setValues(serviceName);
        try {
            baseRepository.save(mainServices);
        }catch (PersistenceException e){
            System.out.println("💥Service saving went wrong");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<MainServices> findByName(String mainServiceName) {
        return baseRepository.findByName(mainServiceName);
    }

    @Override
    public boolean existsByName(String mainServiceName) {
        return baseRepository.existsByName(mainServiceName);
    }

    private void checkConditions(String serviceName) {
        if (existsByName(serviceName)) {
            throw new CustomException("4999", "Duplicate main service name");
        }
    }

    private MainServices setValues(String serviceName) {
        MainServices mainServices = new MainServices();
        mainServices.setName(serviceName);
        mainServices.setSubServices(new ArrayList<>());
        return mainServices;
    }
}
