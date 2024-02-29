package com.Maktab101.SpringProject.service;

import com.Maktab101.SpringProject.dto.users.SearchRequestDto;
import com.Maktab101.SpringProject.model.enums.GlobalOperator;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface FilterSpecification<T> {

     Specification<T> getSpecificationList(List<SearchRequestDto> searchRequestDto, GlobalOperator globalOperator);

}
