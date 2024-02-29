package com.Maktab101.SpringProject.service;


import com.Maktab101.SpringProject.model.SubServices;

import java.util.Optional;

public interface SubServicesService {
    void addService(String subServiceName,double baseWage, String description,String mainServiceName);
    SubServices findByName(String subServiceName);
    boolean existsByName(String subServiceName);
    void editBaseWage(Long serviceId,double newWage);
    void editDescription(Long serviceId,String newDescription);
    void addToSubService(Long technicianId, Long serviceId);
    void deleteFromSubService(Long technicianId, Long serviceId);
    SubServices findById(Long subServiceId);
    SubServices save(SubServices subServices);
}
