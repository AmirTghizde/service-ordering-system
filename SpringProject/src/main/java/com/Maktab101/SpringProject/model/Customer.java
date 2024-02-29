package com.Maktab101.SpringProject.model;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer extends User {


    private double balance;

    private Long ordersSubmitted;

    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private List<Order> orders;
}
