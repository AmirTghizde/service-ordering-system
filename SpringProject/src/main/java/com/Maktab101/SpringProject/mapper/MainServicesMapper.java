package com.Maktab101.SpringProject.mapper;

import com.Maktab101.SpringProject.dto.MainServicesResponseDto;
import com.Maktab101.SpringProject.model.MainServices;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MainServicesMapper {
    MainServicesMapper INSTANCE = Mappers.getMapper(MainServicesMapper.class);
    MainServices toEntity(MainServicesResponseDto mainServicesResponseDto);

    @AfterMapping
    default void linkSubServices(@MappingTarget MainServices mainServices) {
        mainServices.getSubServices().forEach(subService -> subService.setMainServices(mainServices));
    }

    MainServicesResponseDto toDto(MainServices mainServices);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MainServices partialUpdate(MainServicesResponseDto mainServicesResponseDto, @MappingTarget MainServices mainServices);
}