package com.Maktab101.SpringProject.mapper;

import com.Maktab101.SpringProject.dto.services.SubServicesResponseDto;
import com.Maktab101.SpringProject.model.SubServices;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubServicesMapper {
    SubServicesMapper INSTANCE = Mappers.getMapper(SubServicesMapper.class);

    SubServices toEntity(SubServicesResponseDto subServicesResponseDto);

    SubServicesResponseDto toDto(SubServices subServices);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SubServices partialUpdate(SubServicesResponseDto subServicesResponseDto, @MappingTarget SubServices subServices);
}