package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.base.service.BaseEntityServiceImpl;
import ir.maktabSharif101.finalProject.entity.MainServices;
import ir.maktabSharif101.finalProject.repository.MainServicesRepository;
import ir.maktabSharif101.finalProject.service.MainServicesService;
import ir.maktabSharif101.finalProject.utils.CustomException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
public class MainServicesServiceImpl extends BaseEntityServiceImpl<MainServices, Long, MainServicesRepository>
        implements MainServicesService {
    public MainServicesServiceImpl(MainServicesRepository baseRepository) {
        super(baseRepository);
    }

    @Override
    public void addService(String serviceName) {
        log.info("Adding a new service named [{}]",serviceName);
        checkConditions(serviceName);
        try {
            MainServices mainServices = setValues(serviceName);
            log.info("Connecting to [{}]",baseRepository);
            baseRepository.save(mainServices);
        } catch (PersistenceException e) {
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
        log.info("Checking main service conditions");
        if (existsByName(serviceName)) {
            log.error("[{}] already exists in database throwing exception",serviceName);
            throw new CustomException("DuplicateMainService", "Main service already exists in the database");
        }
    }

    private MainServices setValues(String serviceName) {
        log.info("Setting main services values");
        MainServices mainServices = new MainServices();
        mainServices.setName(serviceName);
        return mainServices;}}