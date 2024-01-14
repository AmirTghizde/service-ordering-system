package ir.maktabSharif101.finalProject.entity;

import ir.maktabSharif101.finalProject.base.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "main_services")
public class MainServices extends BaseEntity<Long> {
    @Column(unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "mainServices")
    @ToString.Exclude
    private List<SubServices> subServices  = new ArrayList<>();

}
