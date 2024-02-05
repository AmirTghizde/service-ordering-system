package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.dto.users.SearchRequestDto;
import com.Maktab101.SpringProject.model.enums.GlobalOperator;
import com.Maktab101.SpringProject.service.FilterSpecification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterSpecificationImpl<T> implements FilterSpecification<T> {

    @Override
    public Specification<T> getSpecificationList(List<SearchRequestDto> dtoList, GlobalOperator operator) {
        List<Predicate> predicateList = new ArrayList<>();

        return (root, query, criteriaBuilder) -> {
            for (SearchRequestDto dto : dtoList) {
                Predicate predicate = criteriaBuilder.equal(root.get(dto.getColumn()), dto.getValue());
                predicateList.add(predicate);
            }

            if (operator.equals(GlobalOperator.AND)){
                return criteriaBuilder.and(predicateList.toArray(predicateList.toArray(new Predicate[0])));
            }
            return criteriaBuilder.or(predicateList.toArray(predicateList.toArray(new Predicate[0])));
        };
    }
}
