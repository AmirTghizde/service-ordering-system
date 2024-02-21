package com.Maktab101.SpringProject.mapper;

import com.Maktab101.SpringProject.dto.users.*;
import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.Manager;
import com.Maktab101.SpringProject.model.Technician;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    Manager toManager(ManagerResponseDto managerResponseDto);

    ManagerResponseDto toManagerDto(Manager manager);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Manager partialUpdate(ManagerResponseDto managerResponseDto, @MappingTarget Manager manager);

    Customer toCustomer(CustomerResponseDto customerResponseDto);

    @AfterMapping
    default void linkOrders(@MappingTarget Customer customer) {
        customer.getOrders().forEach(order -> order.setCustomer(customer));
    }

    CustomerResponseDto toCustomerDto(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer partialUpdate(CustomerResponseDto customerResponseDto, @MappingTarget Customer customer);

    Technician toTechnician(TechnicianResponseDto technicianResponseDto);

    TechnicianResponseDto toTechnicianDto(Technician technician);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Technician partialUpdate(TechnicianResponseDto technicianResponseDto, @MappingTarget Technician technician);

    Customer toEntity(CustomerDataDto customerDataDto);

    CustomerDataDto toCustomerDataDto(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer partialUpdate(CustomerDataDto customerDataDto, @MappingTarget Customer customer);

    Technician toEntity(TechnicianDataDto technicianDataDto);

    TechnicianDataDto toTechnicianDataDto(Technician technician);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Technician partialUpdate(TechnicianDataDto technicianDataDto, @MappingTarget Technician technician);
}