package ir.maktabSharif101.finalProject.entity;

import ir.maktabSharif101.finalProject.base.entity.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "suggestion")
public class Suggestion extends BaseEntity<Long> {

    private Date suggestionDate;
    private double suggestedPrice;
    private LocalTime suggestedTime;
    private String Duration;

    @ManyToOne
    private Technician technician;

    @ManyToOne
    private Order order;
}
