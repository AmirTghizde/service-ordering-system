package com.Maktab101.SpringProject.model;


import com.Maktab101.SpringProject.model.enums.TechnicianStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "technician")
public class Technician extends User {


    @Enumerated(EnumType.STRING)
    private TechnicianStatus status;

    private double score;

    private double balance;

    @Column(name = "image_data", columnDefinition = "bytea")
    private byte[] imageData;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Technician_SubServices",
            joinColumns = @JoinColumn(name = "Technician_id"),
            inverseJoinColumns = @JoinColumn(name = "SubService_id")
    )
    @ToString.Exclude
    private List<SubServices> subServices = new ArrayList<>();

}
