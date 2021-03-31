package com.example.springproject.entity;

import com.example.springproject.emailvalidation.registration.RegistrationRequest;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Size(min = 6, max = 50)
    // @NonNull
//    @Column(unique = true)
    @Column
    private String userName;

   //  @NonNull
   // @Pattern(regexp = "^[a-zA-Z0-9.\\-\\/+=@_ ]*$")
   // @Size(min = 6)
    @Column
    private String password;

    @Column
//    @UniqueElements//IMPORTANT
    // @NonNull
    private String firstName;

    @Column
    // @NonNull
    private String lastName;

    // @NonNull
    // @Column(unique = true)
    // @Pattern(regexp = "^[0-9]*$")
    // @Size(min = 10, max = 10)
    private String nationalCode;

    // @Pattern(regexp="(^$|[0-9]{10})")
    // @NonNull
    //@Column(unique = true)
    private String phoneNumber;

    private int gradeUser;

    @Enumerated(EnumType.STRING)
    private UserRole roles=UserRole.ROLE_USER;

    // @Email
    @Column
    // @NonNull
    private String email;

    //*****MAYBE CREATE PROBLEM*****//
    private Boolean locked=false;
    @Column
    private Boolean enabled=false;

    private Boolean funny=true;
    private Integer numberProgressInvesment=0;

    public AppUser(String lastName
                    , String email
                    , String password
                    ,String userName
                    , UserRole appUserRole)
    {
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.roles = appUserRole;
        this.userName=userName;

    }
    public AppUser(String email, UserRole appUserRole)
    {
        this.email = email;
        this.roles = appUserRole;
    }

    public AppUser(AppUser user)
    {
        this.lastName = user.lastName;
        this.password = user.password;
        this.email = user.email;
        this.roles =user.roles;
        this.userName=user.userName;
        this.nationalCode=user.nationalCode;
        this.phoneNumber=user.phoneNumber;
    }

    public AppUser(RegistrationRequest user,UserRole roles)
    {
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.roles=roles;
        this.userName=user.getUserName();
        this.nationalCode=user.getNationalCode();
        this.phoneNumber=user.getPhoneNumber();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        SimpleGrantedAuthority authority=new SimpleGrantedAuthority(roles.toString());
        return Collections.singleton(authority);
    }


    public boolean getEnable() {
        return enabled;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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

    return  funny;

    }

    public  void grantAuthority(UserRole role)
    {
        this.roles=role;
    }

}
