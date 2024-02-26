package com.Maktab101.SpringProject.dto.order;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.Order}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishOrderDto implements Serializable {
    @NotNull
    private Long id;
    @Min(value = 0)
    @Max(value = 5)
    private double point;
    private String comment;
}