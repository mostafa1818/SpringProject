package com.example.springproject.emailvalidation.registration;

import com.example.springproject.entity.AppUser;
import com.example.springproject.entity.UserRole;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Setter
@ToString
public class RegistrationRequest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private   String firstName;
    private   String lastName;
    private   String email;
    private   String password;
    private   String userName;
    private   String nationalCode;
    private   String phoneNumber;

    public RegistrationRequest(AppUser user ) {

        this.email=user.getEmail();
        this.firstName=user.getFirstName();
        this.lastName=user.getLastName();
        this.userName=user.getUsername();
        this.phoneNumber=user.getPhoneNumber();
        this.nationalCode=user.getNationalCode();
    }
    public RegistrationRequest(String userName ,String email) {

        this.email=email;
        this.userName=userName;
    }
}