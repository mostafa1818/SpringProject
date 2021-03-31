package com.example.springproject.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
/////////////////should   be  clear///////////////////////////
@Setter
@Getter
@Entity
public class Investor implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 6, max = 50)
    @NonNull
    @Column(unique = true)
    private String username;

    @NonNull
    @Pattern(regexp = "^[a-zA-Z0-9.\\-\\/+=@_ ]*$")
    @Size(min = 6)
    private String password;

    @Column
    @NonNull
    private String firstName;

    @Column
    @NonNull
    private String lastName;

    @NonNull
    @Column(unique = true)
    @Pattern(regexp = "^[0-9]*$")
    @Size(min = 10, max = 10)
    private String nationalCode;

    @Pattern(regexp="(^$|[0-9]{10})")
    @NonNull
    @Column(unique = true)
    private String phoneNumber;

    @Email
    @Column
    @NonNull
    private String email;

    @Enumerated(EnumType.STRING)

    private  UserRole  roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        SimpleGrantedAuthority authority=new SimpleGrantedAuthority(roles.toString());

        return Collections.singleton(authority);
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
