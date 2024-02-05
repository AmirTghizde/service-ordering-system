package com.Maktab101.SpringProject.dto.users;

import com.Maktab101.SpringProject.model.enums.GlobalOperator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private List<SearchRequestDto> searchRequestDto;
    private GlobalOperator globalOperator;
}
