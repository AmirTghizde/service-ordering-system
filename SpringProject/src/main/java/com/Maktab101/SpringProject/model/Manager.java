package com.Maktab101.SpringProject.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "manager")
public class Manager extends User {
    private String managerCode;
}
