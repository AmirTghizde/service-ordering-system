package com.Maktab101.SpringProject.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "suggestion")
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime Date;

    private double suggestedPrice;

    private LocalDateTime suggestedDate;

    private LocalTime Duration;

    @ManyToOne
    private Technician technician;

    @ManyToOne
    private Order order;
}
