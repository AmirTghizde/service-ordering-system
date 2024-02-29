package com.Maktab101.SpringProject.model;

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
@Table(name = "sub_service")
public class SubServices {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    private double baseWage;

    private String description;

    @ManyToOne
    private MainServices mainServices;

    @ManyToMany(mappedBy = "subServices")
    @ToString.Exclude
    private List<Technician> technicians = new ArrayList<>();

    @OneToMany(mappedBy = "subServices")
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

}
