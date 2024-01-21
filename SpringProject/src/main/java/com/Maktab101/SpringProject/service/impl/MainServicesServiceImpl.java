package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.MainServices;
import com.Maktab101.SpringProject.model.SubServices;
import com.Maktab101.SpringProject.repository.MainServicesRepository;
import com.Maktab101.SpringProject.service.MainServicesService;
import com.Maktab101.SpringProject.utils.CustomException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MainServicesServiceImpl implements MainServicesService {

    private final MainServicesRepository mainServicesRepository;

    @Autowired
    public MainServicesServiceImpl(MainServicesRepository mainServicesRepository) {
        this.mainServicesRepository = mainServicesRepository;
    }

    @Override
    public void addService(String serviceName) {
        log.info("Adding a new service named [{}]",serviceName);
        checkConditions(serviceName);
        try {
            MainServices mainServices = setValues(serviceName);
            log.info("Connecting to [{}]",mainServicesRepository);
            mainServicesRepository.save(mainServices);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred printing ... ");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<MainServices> findByName(String mainServiceName) {
        log.info("trying to find [{}]",mainServiceName);
        return mainServicesRepository.findByName(mainServiceName);
    }

    @Override
    public boolean existsByName(String mainServiceName) {
        log.info("trying to check if [{}] exists",mainServiceName);
        return mainServicesRepository.existsByName(mainServiceName);
    }

    @Override
    public MainServices save(MainServices mainServices) {
        return mainServicesRepository.save(mainServices);
    }

    @Override
    public List<MainServices> findAll() {
        return mainServicesRepository.findAll();
    }

    @Override
    public List<String> findSubServiceNames(Long mainServiceId) {
        MainServices mainServices = mainServicesRepository.findById(mainServiceId).orElseThrow(() ->
                new CustomException("MainServiceNotFound", "We cannot find the main service"));

        return mainServices.getSubServices().stream()
                .map(SubServices::getName)
                .collect(Collectors.toList());
    }

    private void checkConditions(String serviceName) {
        log.info("Checking main service conditions");
        if (existsByName(serviceName)) {
            log.error("[{}] already exists in database throwing exception",serviceName);
            throw new CustomException("DuplicateMainService", "Main service already exists in the database");
        }if (StringUtils.isBlank(serviceName)){
            log.error("Main service is blank throwing exception");
            throw new CustomException("InvalidServiceName", "Main service must not be blank");
        }
    }

    private MainServices setValues(String serviceName) {
        log.info("Setting main services values");
        MainServices mainServices = new MainServices();
        mainServices.setName(serviceName);
        return mainServices;}}