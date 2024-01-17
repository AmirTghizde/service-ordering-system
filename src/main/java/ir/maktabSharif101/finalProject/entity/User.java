package ir.maktabSharif101.finalProject.entity;

import com.sun.istack.NotNull;
import ir.maktabSharif101.finalProject.base.entity.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class User extends BaseEntity<Long> {

    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String emailAddress;
    @NotNull
    private String password;
    private LocalDateTime registerDate= LocalDateTime.now();

    public User(String firstname, String lastname, String emailAddress, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.password = password;
    }
}
