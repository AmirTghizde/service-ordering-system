package ir.maktabSharif101.finalProject.entity;

import ir.maktabSharif101.finalProject.entity.enums.TechnicianStatus;
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
@Table(name = "technician")
public class Technician extends User {
    @Enumerated(EnumType.STRING)
    private TechnicianStatus status;
    private double score;
    private double balance;

    @Column(name = "image_data", columnDefinition = "bytea")
    private byte[] imageData;

    @ManyToMany
    @JoinTable(
            name = "Technician_SubServices",
            joinColumns = @JoinColumn(name = "Technician_id"),
            inverseJoinColumns = @JoinColumn(name = "SubService_id")
    )
    private List<SubServices> subServices = new ArrayList<>();

}
