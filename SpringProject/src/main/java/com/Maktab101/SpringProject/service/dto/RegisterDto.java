package com.Maktab101.SpringProject.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDto implements Serializable {

    @Size(min = 3, max = 50, message = "firstname size must be between 3 to 50 letters")
    @Pattern(regexp = "^[^0-9]+$", message = "Invalid firstname")
    String firstname;

    @Pattern(regexp = "^[^0-9]+$", message = "Invalid lastname")
    String lastname;

//    @Email(message = "Invalid Email format")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@gmail.com$",message="Invalid email format")
    String emailAddress;

    @NotBlank(message = "password must not be empty")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$"
            , message = "password must be a combination of letters and numbers")
    String password;

}