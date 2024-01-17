package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.base.service.BaseEntityServiceImpl;
import ir.maktabSharif101.finalProject.entity.MainServices;
import ir.maktabSharif101.finalProject.entity.SubServices;
import ir.maktabSharif101.finalProject.entity.Technician;
import ir.maktabSharif101.finalProject.entity.enums.TechnicianStatus;
import ir.maktabSharif101.finalProject.repository.SubServicesRepository;
import ir.maktabSharif101.finalProject.service.MainServicesService;
import ir.maktabSharif101.finalProject.service.SubServicesService;
import ir.maktabSharif101.finalProject.service.TechnicianService;
import ir.maktabSharif101.finalProject.utils.CustomException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PersistenceException;
import java.util.Optional;

@Slf4j
public class SubServicesServiceImpl extends BaseEntityServiceImpl<SubServices, Long, SubServicesRepository>
        implements SubServicesService {
    private final MainServicesService mainServicesService;
    private final TechnicianService technicianService;

    public SubServicesServiceImpl(SubServicesRepository baseRepository, MainServicesService mainServicesService
            , TechnicianService technicianService) {
        super(baseRepository);
        this.mainServicesService = mainServicesService;
        this.technicianService = technicianService;
    }

    @Override
    public void addService(String serviceName, double baseWage, String description, String mainServiceName) {
        log.info("Adding a new sub service named [{}]", serviceName);
        checkConditions(serviceName, mainServiceName);
        SubServices subServices = setValues(serviceName, baseWage, description);

        MainServices mainServices = mainServicesService.findByName(mainServiceName).orElseThrow(() ->
                new CustomException("MainServiceNotFound", "We can not find the main service"));

        try {
            log.info("Connecting to [{}]", baseRepository);
            baseRepository.beginTransaction();
            subServices.setMainServices(mainServices);
            mainServices.getSubServices().add(subServices);
            mainServicesService.save(mainServices);
            baseRepository.save(subServices);
            baseRepository.commitTransaction();
        } catch (PersistenceException e) {
            baseRepository.rollbackTransaction();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<SubServices> findByName(String subServiceName) {
        log.info("trying to find [{}]",subServiceName);
        return baseRepository.findByName(subServiceName);
    }

    @Override
    public boolean existsByName(String subServiceName) {
        log.info("trying to check if [{}] exists",subServiceName);
        return baseRepository.existsByName(subServiceName);
    }

    @Override
    public void editBaseWage(Long serviceId, double newWage) {
        SubServices subServices = findSubServices(serviceId);
        log.info("Changing [{}] wage from [{}] to [{}]", subServices.getName(), subServices.getBaseWage(), newWage);
        try {
            log.info("Connecting to [{}]", baseRepository);
            subServices.setBaseWage(newWage);
            baseRepository.save(subServices);
        } catch (PersistenceException e) {
            baseRepository.rollbackTransaction();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void editDescription(Long serviceId, String newDescription) {
        SubServices subServices = findSubServices(serviceId);
        log.info("Changing [{}] description from [{}] to [{}]", subServices.getName(), subServices.getDescription(), newDescription);
        try {
            log.info("Connecting to [{}]", baseRepository);
            subServices.setDescription(newDescription);
            baseRepository.save(subServices);
        } catch (PersistenceException e) {
            baseRepository.rollbackTransaction();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addToSubService(Long technicianId, Long serviceId) {
        try {
            baseRepository.beginTransaction();
            //get the entities
            SubServices subService = findSubServices(serviceId);
            Technician technician = findTechnician(technicianId);
            log.info("Adding [{}] to [{}]", technician.getEmailAddress(), subService.getName());
            if (!technician.getStatus().equals(TechnicianStatus.CONFIRMED)) {
                log.error("[{}] is not confirmed throwing exception ", technician.getEmailAddress());
                throw new CustomException("InvalidTechnician", "Technician must be confirmed first");
            }

            if (!subService.getTechnicians().contains(technician)) {
                log.info("Connecting to [{}]",baseRepository);
                //add them
                subService.getTechnicians().add(technician);
                technician.getSubServices().add(subService);

                //save them
                baseRepository.save(subService);
                technicianService.save(technician);
                baseRepository.commitTransaction();
            } else {
                log.error("[{}] already exists throwing Exception",technician.getEmailAddress());
                throw new CustomException("TechnicianAlreadyExists", "You already added this technician before");
            }
        } catch (PersistenceException e) {
            baseRepository.rollbackTransaction();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteFromSubService(Long technicianId, Long serviceId) {
        try {
            baseRepository.beginTransaction();
            //get the entities
            SubServices subService = findSubServices(serviceId);
            Technician technician = findTechnician(technicianId);
            log.info("deleting [{}] from [{}]", technician.getEmailAddress(), subService.getName());

            if (subService.getTechnicians().contains(technician)) {
                log.info("Connecting to [{}]",baseRepository);
                //add them
                subService.getTechnicians().remove(technician);
                technician.getSubServices().remove(subService);

                //save them
                baseRepository.save(subService);
                technicianService.save(technician);
                baseRepository.commitTransaction();
            } else {
                log.error("[{}] doesn't exists throwing Exception",technician.getEmailAddress());
                throw new CustomException("TechnicianDoesntExist", "Sub service doesn't have that technician");
            }
        } catch (PersistenceException e) {
            baseRepository.rollbackTransaction();
            System.out.println(e.getMessage());
        }
    }

    private void checkConditions(String serviceName, String mainServiceName) {
        log.info("Checking conditions");
        if (existsByName(serviceName)) {
            log.error("[{}] already exists in database throwing exception",serviceName);
            throw new CustomException("DuplicateSubService", "Sub service already exists in the database");
        }
        if (!mainServicesService.existsByName(mainServiceName)) {
            log.error("[{}] doesnt exist in database throwing exception",mainServiceName);
            throw new CustomException("MainServiceNotFound", "We can not find the main service");
        }
    }

    private SubServices setValues(String serviceName, double baseWage, String description) {
        log.info("Setting sub service values");
        SubServices subServices = new SubServices();
        subServices.setName(serviceName);
        subServices.setBaseWage(baseWage);
        subServices.setDescription(description);
        return subServices;
    }

    private Technician findTechnician(Long technicianId) {
        return technicianService.findById(technicianId).orElseThrow(
                () -> new CustomException("TechnicianNotFound", "We can't find that technician"));
    }

    private SubServices findSubServices(Long serviceId) {
        return findById(serviceId).
                orElseThrow(() -> new CustomException("SubServiceNotFound", "We can not find the sub service"));
    }
}
