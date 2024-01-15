package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.base.service.BaseEntityServiceImpl;
import ir.maktabSharif101.finalProject.entity.MainServices;
import ir.maktabSharif101.finalProject.entity.SubServices;
import ir.maktabSharif101.finalProject.entity.Technician;
import ir.maktabSharif101.finalProject.repository.SubServicesRepository;
import ir.maktabSharif101.finalProject.service.MainServicesService;
import ir.maktabSharif101.finalProject.service.SubServicesService;
import ir.maktabSharif101.finalProject.service.TechnicianService;
import ir.maktabSharif101.finalProject.utils.CustomException;

import javax.persistence.PersistenceException;
import java.util.Optional;

public class SubServicesServiceImpl extends BaseEntityServiceImpl<SubServices, Long, SubServicesRepository>
        implements SubServicesService {
    private final MainServicesService mainServicesService;
    private final TechnicianService technicianService;

    public SubServicesServiceImpl(SubServicesRepository baseRepository, MainServicesService mainServicesService
    ,TechnicianService technicianService) {
        super(baseRepository);
        this.mainServicesService = mainServicesService;
        this.technicianService = technicianService;
    }

    @Override
    public void addService(String serviceName, double baseWage, String description, String mainServiceName) {
        checkConditions(serviceName, mainServiceName);
        SubServices subServices = setValues(serviceName, baseWage, description);
        MainServices mainServices = findMainService(mainServiceName);

        try {
            baseRepository.beginTransaction();
            subServices.setMainServices(mainServices);
            mainServices.getSubServices().add(subServices);
            mainServicesService.save(mainServices);
            baseRepository.save(subServices);
            baseRepository.commitTransaction();
        } catch (PersistenceException e) {
            baseRepository.rollbackTransaction();
            System.out.println("💥Service saving went wrong");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<SubServices> findByName(String subServiceName) {
        return baseRepository.findByName(subServiceName);
    }

    @Override
    public boolean existsByName(String subServiceName) {
        return baseRepository.existsByName(subServiceName);
    }

    @Override
    public void editBaseWage(Long serviceId, double newWage) {
        SubServices subServices = findById(serviceId).
                orElseThrow(() -> new CustomException("4404", "Sub service not found"));
        subServices.setBaseWage(newWage);
        try{
            baseRepository.save(subServices);
        }catch (PersistenceException e){
            baseRepository.rollbackTransaction();
            System.out.println("💥Service saving went wrong");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void editDescription(Long serviceId, String newDescription) {
        SubServices subServices = findById(serviceId).
                orElseThrow(() -> new CustomException("4404", "Sub service not found"));
        subServices.setDescription(newDescription);
        try{
            baseRepository.save(subServices);
        }catch (PersistenceException e){
            baseRepository.rollbackTransaction();
            System.out.println("💥Service saving went wrong");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addTechnician(Long technicianId, String subservienceName) {
        try {
            baseRepository.beginTransaction();
            //get the entities
            SubServices subService = baseRepository.findByName(subservienceName).orElseThrow(
                    () -> new CustomException("SubServiceNotFound", "We can't find the sub service"));
            Technician technician = technicianService.findById(technicianId).orElseThrow(
                    () -> new CustomException("TechnicianNotFound", "We can't find that technician"));

            if (!subService.getTechnicians().contains(technician)){
                //add them
                subService.getTechnicians().add(technician);
                technician.getSubServices().add(subService);

                //save them
                baseRepository.save(subService);
                technicianService.save(technician);
                baseRepository.commitTransaction();
            }else {
                throw new CustomException("TechnicianAlreadyExists","You already added this technician before");
            }
        }catch (PersistenceException e){
            baseRepository.rollbackTransaction();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteTechnician(Long technicianId, String subservienceName) {
        try{
            baseRepository.beginTransaction();
            //get the entities
            SubServices subService = baseRepository.findByName(subservienceName).orElseThrow(
                    () -> new CustomException("SubServiceNotFound", "We can't find the sub service"));
            Technician technician = technicianService.findById(technicianId).orElseThrow(
                    () -> new CustomException("TechnicianNotFound", "We can't find that technician"));
            if (subService.getTechnicians().contains(technician)){
                //add them
                subService.getTechnicians().remove(technician);
                technician.getSubServices().remove(subService);

                //save them
                baseRepository.save(subService);
                technicianService.save(technician);
                baseRepository.commitTransaction();
            }else {
                throw new CustomException("TechnicianDoesntExist","Sub service doesn't have that technician");
            }


        }catch (PersistenceException e){
            baseRepository.rollbackTransaction();
            System.out.println(e.getMessage());
        }
    }

    private void checkConditions(String serviceName, String mainServiceName) {
        if (existsByName(serviceName)) {
            throw new CustomException("4999", "Duplicate sub service name");
        }
        if (!mainServicesService.existsByName(mainServiceName)) {
            throw new CustomException("4404", "Main service not found");
        }
    }

    private SubServices setValues(String serviceName, double baseWage, String description) {
        SubServices subServices = new SubServices();
        subServices.setName(serviceName);
        subServices.setBaseWage(baseWage);
        subServices.setDescription(description);
        return subServices;
    }

    private MainServices findMainService(String mainServiceName) {
        return mainServicesService.findByName(mainServiceName).orElseThrow(() ->
                new CustomException("4004", "Unable to get main service"));
    }
}
