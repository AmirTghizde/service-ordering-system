package com.Maktab101.SpringProject.dto.suggestion;

import com.Maktab101.SpringProject.dto.users.TechnicianDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.Suggestion}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionDto implements Serializable {
    private Long id;
    private LocalDateTime Date;
    private double suggestedPrice;
    private LocalDateTime suggestedDate;
    private LocalTime Duration;
    private TechnicianDto technician;
}