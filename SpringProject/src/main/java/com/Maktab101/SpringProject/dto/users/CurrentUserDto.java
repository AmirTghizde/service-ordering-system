package com.Maktab101.SpringProject.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.Maktab101.SpringProject.model.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserDto implements Serializable {
    private String firstname;
    private String lastname;
    private String email;
}