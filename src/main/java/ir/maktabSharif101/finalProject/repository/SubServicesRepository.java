package ir.maktabSharif101.finalProject.repository;

import ir.maktabSharif101.finalProject.base.repository.BaseEntityRepository;
import ir.maktabSharif101.finalProject.entity.SubServices;

import java.util.Optional;

public interface SubServicesRepository extends BaseEntityRepository<SubServices,Long> {
    Optional<SubServices> findByName(String subServiceName);
    boolean existsByName(String subServiceName);
}
