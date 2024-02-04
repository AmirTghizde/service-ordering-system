package com.Maktab101.SpringProject.mapper;

import com.Maktab101.SpringProject.dto.SuggestionResponseDto;
import com.Maktab101.SpringProject.model.Suggestion;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SuggestionMapper {

    SuggestionMapper INSTANCE = Mappers.getMapper(SuggestionMapper.class);
    Suggestion toEntity(SuggestionResponseDto suggestionResponseDto);

    SuggestionResponseDto toDto(Suggestion suggestion);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Suggestion partialUpdate(SuggestionResponseDto suggestionResponseDto, @MappingTarget Suggestion suggestion);
}