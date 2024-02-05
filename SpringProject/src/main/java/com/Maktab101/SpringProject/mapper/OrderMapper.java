package com.Maktab101.SpringProject.mapper;

import com.Maktab101.SpringProject.dto.order.OrderCommentDto;
import com.Maktab101.SpringProject.dto.order.OrderResponseDto;
import com.Maktab101.SpringProject.model.Order;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toEntity(OrderResponseDto orderResponseDto);

    OrderResponseDto toDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(OrderResponseDto orderResponseDto, @MappingTarget Order order);

    Order toEntity(OrderCommentDto orderCommentDto);

    OrderCommentDto toOrderCommentDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(OrderCommentDto orderCommentDto, @MappingTarget Order order);
}