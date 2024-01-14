package ir.maktabSharif101.finalProject.repository;

import ir.maktabSharif101.finalProject.base.repository.BaseEntityRepository;
import ir.maktabSharif101.finalProject.entity.MainServices;

import java.util.Optional;

public interface MainServicesRepository extends BaseEntityRepository<MainServices,Long> {
    Optional<MainServices> findByName(String mainServiceName);
    boolean existsByName(String mainServiceName);
}
