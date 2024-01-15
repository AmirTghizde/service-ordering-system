package ir.maktabSharif101.finalProject.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer extends User {
    private double balance = 0;
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

}
