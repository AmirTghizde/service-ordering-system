package ir.maktabSharif101.finalProject.entity;

import ir.maktabSharif101.finalProject.base.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sub_service")
public class SubServices extends BaseEntity<Long> {

    @Column(unique = true)
    private String name;
    private double baseWage;
    private String description;

    @ManyToOne
    private MainServices mainServices;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "subServices")
    private List<Technician> technicians = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "subServices")
    private List<Order> orders = new ArrayList<>();

}
