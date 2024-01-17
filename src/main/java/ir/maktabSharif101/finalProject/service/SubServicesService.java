package ir.maktabSharif101.finalProject.service;

import ir.maktabSharif101.finalProject.base.service.BaseEntityService;
import ir.maktabSharif101.finalProject.entity.MainServices;
import ir.maktabSharif101.finalProject.entity.SubServices;

import java.util.Optional;

public interface SubServicesService extends BaseEntityService<SubServices,Long> {
    void addService(String subServiceName,double baseWage, String description,String mainServiceName);
    Optional<SubServices> findByName(String subServiceName);
    boolean existsByName(String subServiceName);
    void editBaseWage(Long serviceId,double newWage);
    void editDescription(Long serviceId,String newDescription);
    void addTechnician(Long technicianId,Long serviceId);
    void deleteTechnician(Long technicianId,Long serviceId);
}
