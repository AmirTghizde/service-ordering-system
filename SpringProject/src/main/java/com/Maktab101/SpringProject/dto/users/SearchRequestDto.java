package com.Maktab101.SpringProject.dto.users;

import com.Maktab101.SpringProject.model.enums.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDto {
    private String column;
    private String value;
    private Operation operation;
    private String joinTable;
}
