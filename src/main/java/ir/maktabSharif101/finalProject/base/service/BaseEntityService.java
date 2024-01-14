package ir.maktabSharif101.finalProject.base.service;

import ir.maktabSharif101.finalProject.base.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseEntityService<T extends BaseEntity<ID>, ID extends Serializable> {
    T save(T t);
    List<T> findAll();
    long count();
    Optional<T> findById(ID id);
    void deleteAll();
    void deleteById(ID id);
    boolean existsById(ID id);
}