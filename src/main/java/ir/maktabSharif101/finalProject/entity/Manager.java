package ir.maktabSharif101.finalProject.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "manager")
public class Manager extends User {
    private Date lastLogin = new Date();
}
