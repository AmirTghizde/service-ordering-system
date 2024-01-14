package ir.maktabSharif101.finalProject.base.service;
import ir.maktabSharif101.finalProject.base.entity.BaseEntity;
import ir.maktabSharif101.finalProject.base.repository.BaseEntityRepository;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseEntityServiceImpl<T extends BaseEntity<ID>, ID extends Serializable,
        R extends BaseEntityRepository<T, ID>>
        implements BaseEntityService<T, ID> {

    protected final R baseRepository;

    @Override
    public T save(T t) {
        return baseRepository.save(t);
    }

    @Override
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Override
    public long count() {
        return baseRepository.count();
    }

    @Override
    public Optional<T> findById(ID id) {
        return baseRepository.findById(id);
    }

    @Override
    public void deleteAll() {
        baseRepository.deleteAll();
    }

    @Override
    public void deleteById(ID id) {
        baseRepository.deleteById(id);
    }

    @Override
    public boolean existsById(ID id) {
        return baseRepository.existsById(id);
    }
}