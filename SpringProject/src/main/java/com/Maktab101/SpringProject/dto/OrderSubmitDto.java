package com.Maktab101.SpringProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderSubmitDto {

    @NotNull(message = "you must specify the service")
    Long subServiceId;

    String jobInfo;

    @NotBlank(message = "Date must be specified")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message = "The correct date format is (YYYY-MM-DD)")
    String date;

    @NotBlank(message = "Time must be specified")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",message = "The correct date format is (HH:MM) .. pst it European ")
    String time;

    @NotBlank(message = "Address must be specified")
    String Address;

    @Positive
    double price;
}
