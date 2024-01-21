package com.Maktab101.SpringProject.model;


import com.Maktab101.SpringProject.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
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
public class Order  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
