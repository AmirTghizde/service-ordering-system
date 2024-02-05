package com.Maktab101.SpringProject.service.impl;


import com.Maktab101.SpringProject.model.MainServices;
import com.Maktab101.SpringProject.model.SubServices;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.model.enums.TechnicianStatus;
import com.Maktab101.SpringProject.repository.SubServicesRepository;
import com.Maktab101.SpringProject.service.MainServicesService;
import com.Maktab101.SpringProject.service.SubServicesService;
import com.Maktab101.SpringProject.service.TechnicianService;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import com.Maktab101.SpringProject.utils.exceptions.DuplicateValueException;
import com.Maktab101.SpringProject.utils.exceptions.NotFoundException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SubServicesServiceImpl implements SubServicesService {
    private final MainServicesService mainServicesService;
    private final TechnicianService technicianService;
    private final SubServicesRepository subServicesRepository;

    @Autowired
    public SubServicesServiceImpl(MainServicesService mainServicesService, TechnicianService technicianService,
                                  SubServicesRepository subServicesRepository) {
        this.mainServicesService = mainServicesService;
        this.technicianService = technicianService;
        this.subServicesRepository = subServicesRepository;
    }


    @Override
    @Transactional
    public void addService(String serviceName, double baseWage, String description, String mainServiceName) {
        log.info("Adding a new sub service named [{}]", serviceName);
        checkConditions(serviceName, mainServiceName);
        SubServices subServices = setValues(serviceName, baseWage, description);

        MainServices mainServices = mainServicesService.findByName(mainServiceName);

        try {
            log.info("Connecting to [{}]", subServicesRepository);
            subServices.setMainServices(mainServices);
            mainServices.getSubServices().add(subServices);
            mainServicesService.save(mainServices);
            subServicesRepository.save(subServices);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public SubServices findByName(String subServiceName) {
        log.info("trying to find [{}]", subServiceName);
        return subServicesRepository.findByName(subServiceName).orElseThrow(
                () -> new NotFoundException("Couldn't find a subService with this name: " + subServiceName)
        );
    }

    @Override
    public boolean existsByName(String subServiceName) {
        log.info("trying to check if [{}] exists", subServiceName);
        return subServicesRepository.existsByName(subServiceName);
    }

    @Override
    public void editBaseWage(Long serviceId, double newWage) {
        SubServices subServices = findById(serviceId);
        log.info("Changing [{}] wage from [{}] to [{}]", subServices.getName(), subServices.getBaseWage(), newWage);
        try {
            log.info("Connecting to [{}]", subServicesRepository);
            subServices.setBaseWage(newWage);
            subServicesRepository.save(subServices);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public void editDescription(Long serviceId, String newDescription) {
        SubServices subServices = findById(serviceId);
        log.info("Changing [{}] description from [{}] to [{}]", subServices.getName(), subServices.getDescription(), newDescription);
        try {
            log.info("Connecting to [{}]", subServicesRepository);
            subServices.setDescription(newDescription);
            subServicesRepository.save(subServices);
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void addToSubService(Long technicianId, Long serviceId) {
        try {
            //get the entities
            SubServices subService = findById(serviceId);
            Technician technician = technicianService.findById(technicianId);
            log.info("Adding [{}] to [{}]", technician.getEmail(), subService.getName());
            if (!technician.getStatus().equals(TechnicianStatus.CONFIRMED)) {
                log.error("[{}] is not confirmed throwing exception ", technician.getEmail());
                throw new CustomException("Technician must be confirmed first");
            }

            if (!subService.getTechnicians().contains(technician)) {
                log.info("Connecting to [{}]", subServicesRepository);
                //add them
                subService.getTechnicians().add(technician);
                technician.getSubServices().add(subService);

                //save them
                subServicesRepository.save(subService);
                technicianService.save(technician);
            } else {
                log.error("[{}] already exists throwing Exception", technician.getEmail());
                throw new DuplicateValueException("You already added this technician before");
            }
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteFromSubService(Long technicianId, Long serviceId) {
        try {
            //get the entities
            SubServices subService = findById(serviceId);
            Technician technician = technicianService.findById(technicianId);
            log.info("deleting [{}] from [{}]", technician.getEmail(), subService.getName());

            if (subService.getTechnicians().contains(technician)) {
                log.info("Connecting to [{}]", subServicesRepository);
                //remove them
                subService.getTechnicians().remove(technician);
                technician.getSubServices().remove(subService);

                //save them
                subServicesRepository.save(subService);
                technicianService.save(technician);
            } else {
                log.error("[{}] doesn't exists throwing Exception", technician.getEmail());
                throw new NotFoundException("Couldn't find this technician in the subService: " + technician.getEmail());
            }
        } catch (PersistenceException e) {
            log.error("PersistenceException occurred throwing CustomException ... ");
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public SubServices findById(Long subServiceId) {
        return subServicesRepository.findById(subServiceId).
                orElseThrow(() -> new NotFoundException("Couldn't find a subService with this id: " + subServiceId));
    }

    @Override
    public SubServices save(SubServices subServices) {
        return subServicesRepository.save(subServices);
    }

    protected void checkConditions(String serviceName, String mainServiceName) {
        log.info("Checking conditions");
        if (existsByName(serviceName)) {
            log.error("[{}] already exists in database throwing exception", serviceName);
            throw new DuplicateValueException("This service name is already being used in database: " + serviceName);
        }
        if (!mainServicesService.existsByName(mainServiceName)) {
            log.error("[{}] doesnt exist in database throwing exception", mainServiceName);
            throw new NotFoundException("Couldn't find a mainService with this name: " + mainServiceName);
        }
    }

    protected SubServices setValues(String serviceName, double baseWage, String description) {
        log.info("Setting sub service values");
        SubServices subServices = new SubServices();
        subServices.setName(serviceName);
        subServices.setBaseWage(baseWage);
        subServices.setDescription(description);
        return subServices;
    }
}
