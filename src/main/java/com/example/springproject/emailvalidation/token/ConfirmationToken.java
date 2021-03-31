package com.example.springproject.emailvalidation.token;

import com.example.springproject.entity.AppUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString
public class ConfirmationToken
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean enable=true;

    private LocalDateTime confirmedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Token role;

    @ManyToOne
    @JoinColumn(nullable = false,name="Confirmation_Token")
    private AppUser user;

    public ConfirmationToken(
                              String token,
                              LocalDateTime createAt,
                              LocalDateTime expiresAt
                             ,AppUser user
                             ,Token role)
    {
        this.token = token;
        this.createAt = createAt;
        this.expiresAt = expiresAt;
        this.user = user;
        this.role=role;
    }
    public ConfirmationToken(
            ConfirmationToken newToken)
    {
        this.token = newToken.token;
        this.createAt = newToken.createAt;
        this.expiresAt = newToken.expiresAt;
        this.user = newToken.user;
        this.role=newToken.role;
    }
}