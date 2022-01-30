package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    @Email(message = "Please enter a correct email")
    @NotEmpty(message = "Please enter an email")
    @Length(max = 200, message = "Email must be less than 200 characters long")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotBlank(message = "Please enter your first name")
    @NotEmpty(message = "First Name must not be blank")
    @Pattern(regexp = "^[a-zA-Z -]{2,20}$",message = "Please enter a correct first name. No special characters, or numbers. Dashes allowed.")
    private String firstName;

    @NotBlank(message = "Please enter your last name")
    @NotEmpty(message = "Last Name must not be blank")
    @Pattern(regexp = "^[a-zA-Z -]{2,20}$",message = "Please enter a correct last name. No special characters, or numbers. Dashes allowed.")
    private String lastName;

    @Pattern(regexp = "^([1-9]{3})(-)([0-9]{3})(-)([0-9]{4})$",message = "Phone pattern must be 514-888-8888")
    private String phone;

    @Length(max = 64, message = "Password must be at least 6-64 characters long")
    private String password;

    @Length(max = 64, message = "Password must be at least 6-64 characters long")
    private String passwordConfirm;

    @NotBlank(message = "Please enter your address")

    @NotEmpty(message = "Address must not be empty")
    @Pattern(regexp = "^([0-9]{0,7} [a-zA-Z0-9 !@#$%^&*()_+=\\-`~{}\\[\\]<>,./?;':\"|\\\\]+)$",
            message = "Address must be formatted correctly. (Ex. 123 Address). Takes numbers, characters, special characters")
    private String address;
}
