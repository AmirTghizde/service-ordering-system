package com.Maktab101.SpringProject.dto.users;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageSaveDto {
    @NotNull
    private String imageName;
}
