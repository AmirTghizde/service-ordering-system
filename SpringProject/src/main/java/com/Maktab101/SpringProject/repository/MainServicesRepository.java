package com.Maktab101.SpringProject.repository;

import com.Maktab101.SpringProject.model.MainServices;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MainServicesRepository extends JpaRepository<MainServices,Long> {
    Optional<MainServices> findByName(String mainServiceName);
    boolean existsByName(String mainServiceName);
}
