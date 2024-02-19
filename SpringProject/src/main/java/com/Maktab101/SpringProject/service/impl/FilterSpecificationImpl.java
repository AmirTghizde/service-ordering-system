package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.dto.users.SearchRequestDto;
import com.Maktab101.SpringProject.model.enums.GlobalOperator;
import com.Maktab101.SpringProject.service.FilterSpecification;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FilterSpecificationImpl<T> implements FilterSpecification<T> {

    @Override
    public Specification<T> getSpecificationList(List<SearchRequestDto> dtoList, GlobalOperator operator) {
        List<Predicate> predicateList = new ArrayList<>();

        return (root, query, criteriaBuilder) -> {
            for (SearchRequestDto dto : dtoList) {
                switch (dto.getOperation()) {
                    case LIKE -> {
                        Predicate predicate = criteriaBuilder
                                .like(root.get(dto.getColumn()), "%" + dto.getValue() + "%");

                        predicateList.add(predicate);
                    }
                    case EQUAL -> {
                        Predicate predicate = criteriaBuilder
                                .equal(root.get(dto.getColumn()), dto.getValue());

                        predicateList.add(predicate);
                    }
                    case IN -> {
                        String[] strings = dto.getValue().split(",");
                        Predicate predicate = root.get(dto.getColumn()).in(Arrays.asList(strings));

                        predicateList.add(predicate);
                    }
                    case GREATER_THAN -> {
                        Predicate predicate = criteriaBuilder
                                .greaterThan(root.get(dto.getColumn()), dto.getValue());

                        predicateList.add(predicate);
                    }
                    case LESS_THAN -> {
                        Predicate predicate = criteriaBuilder
                                .lessThan(root.get(dto.getColumn()), dto.getValue());

                        predicateList.add(predicate);
                    }
                    case BETWEEN -> {
                        String[] strings = dto.getValue().split(",");
                        Predicate predicate = criteriaBuilder.between(root.get(dto.getColumn()),
                                Long.parseLong(strings[0]), Long.parseLong(strings[1]));

                        predicateList.add(predicate);
                    }
                    case JOIN -> {
                        Predicate predicate = criteriaBuilder.equal(
                                root.join(dto.getJoinTable()).get(dto.getColumn()), dto.getValue());

                        predicateList.add(predicate);
                    }
                    case DOUBLE_JOIN -> {
                        String[] joinTables = dto.getJoinTable().split(",");

                        Predicate predicate = criteriaBuilder.equal(
                                root.join(joinTables[0]).join(joinTables[1]).get(dto.getColumn()),
                                dto.getValue());

                        predicateList.add(predicate);
                    }
                    default -> throw new CustomException("Unexpected argument: " + dto.getOperation());
                }
            }

            if (operator.equals(GlobalOperator.AND)) {
                return criteriaBuilder.and(predicateList.toArray(predicateList.toArray(new Predicate[0])));
            }
            return criteriaBuilder.or(predicateList.toArray(predicateList.toArray(new Predicate[0])));
        };
    }
}
