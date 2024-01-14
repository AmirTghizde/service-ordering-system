package ir.maktabSharif101.finalProject.base.repository;

import ir.maktabSharif101.finalProject.base.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface BaseEntityRepository<T extends BaseEntity<ID>,ID extends Serializable> {

    T save(T t);
    List<T> findAll();
    long count();
    Optional<T> findById(ID id);
    void deleteAll();
    void deleteById(ID id);
    boolean existsById(ID id);

    void beginTransaction();
    void commitTransaction();
    void rollbackTransaction();

}