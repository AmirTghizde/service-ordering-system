package com.Maktab101.SpringProject.repository;

import com.Maktab101.SpringProject.model.SubServices;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SubServicesRepository extends JpaRepository<SubServices,Long> {
    Optional<SubServices> findByName(String subServiceName);
    boolean existsByName(String subServiceName);
}
