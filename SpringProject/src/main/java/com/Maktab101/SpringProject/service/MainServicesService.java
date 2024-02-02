package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.MainServices;

import java.util.List;
import java.util.Optional;

public interface MainServicesService {
    void addService(String serviceName);
    MainServices findByName(String MainServiceName);
    boolean existsByName(String MainServiceName);
    MainServices save(MainServices mainServices);
    List<MainServices> findAll();
    List<String> findSubServiceNames(Long mainServiceId);
}

