package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 200)
    @Email(message = "Please enter a correct email")
    @NotEmpty(message = "Please enter an email")
    private String email;

    @NotNull(message = "Please choose a role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "first_name",nullable = false,length = 20)
    @NotBlank(message = "Please enter your first name")
    @Pattern(regexp = "^[a-zA-Z -]{2,20}$",message = "Please enter a correct first name. No special characters, or numbers. Dashes allowed.")
    private String firstName;

    @Column(name = "last_name",nullable = false,length = 20)
    @NotBlank(message = "Please enter your last name")
    @Pattern(regexp = "^[a-zA-Z -]{2,20}$",message = "Please enter a correct last name. No special characters, or numbers. Dashes allowed.")
    private String lastName;

    @Column(nullable = false, length = 12)
    @Pattern(regexp = "^([1-9]{3})(-)([0-9]{3})(-)([0-9]{4})$",message = "Phone pattern must be 514-888-8888")
    private String phone;

    @Column(nullable = false)
    @Length(min = 6, max = 64)
    //@Pattern(regexp = "^([0-9a-z]{6,20})$",message = "Password must be at least 6 characters long,including lower case letters and digitals")
    private String password;

    @Transient
    private String newPassword;

    @Transient
    private String passwordRepeat;

    @Column(nullable = false)
    @NotBlank(message = "Please enter your address")
    private String address;

    @ManyToOne
    private FileDB image;

    @OneToMany(mappedBy = "student")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<CourseStudent> coursesTaken;

    public User(String email, Role type, String firstName, String lastName, String phone, String password, String address, FileDB image) {
        this.email = email;
        this.role = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.image = image;
    }

    public User(String email, Role type, String firstName, String lastName, String phone, String password, String address) {
        this.email = email;
        this.role = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }

    public User(String email, Role type, String firstName, String lastName, String phone, String address) {
        this.email = email;
        this.role = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    public User(Role type, String firstName, String lastName, String phone, String address) {
        this.role = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return list;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
