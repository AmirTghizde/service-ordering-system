package com.Maktab101.SpringProject.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuggestionDto {

    @NotNull(message = "you must specify the order")
    Long orderID;

    @Positive
    double suggestedPrice;

    @NotBlank(message = "Time must be specified")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",message = "The correct date format is (HH:MM) .. pst it European ")
    String suggestedTime;

    @NotBlank(message = "Date must be specified")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message = "The correct date format is (YYYY-MM-DD)")
    String suggestedDate;

    @NotBlank(message = "Duration must be specified")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",message = "The correct date format is (HH:MM) .. pst it European ")
    String Duration;
}
