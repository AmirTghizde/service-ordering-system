package ir.maktabSharif101.finalProject.service;

import ir.maktabSharif101.finalProject.base.service.BaseEntityService;
import ir.maktabSharif101.finalProject.entity.MainServices;

import java.util.Optional;

public interface MainServicesService extends BaseEntityService<MainServices,Long> {
    void addService(String serviceName);
    Optional<MainServices> findByName(String MainServiceName);
    boolean existsByName(String MainServiceName);
}

