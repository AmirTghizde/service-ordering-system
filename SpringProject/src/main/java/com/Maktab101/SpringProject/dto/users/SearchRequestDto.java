package com.Maktab101.SpringProject.dto.users;

import com.Maktab101.SpringProject.model.enums.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchRequestDto {
    @NotNull
    private String column;
    @NotNull
    private String value;
    @NotNull
    private Operation operation;
    private String joinTable;
}
