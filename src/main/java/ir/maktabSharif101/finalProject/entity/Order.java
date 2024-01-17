package ir.maktabSharif101.finalProject.entity;

import ir.maktabSharif101.finalProject.base.entity.BaseEntity;
import ir.maktabSharif101.finalProject.entity.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity<Long> {

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private String jobInfo;
    private LocalDateTime dateAndTime;
    private String Address;
    private double price;
    private String comment;
    private double point;
    @ManyToOne
    private Customer customer;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "order")
    private List<Suggestion> suggestions= new ArrayList<>();
    @ManyToOne
    private SubServices subServices;
}
