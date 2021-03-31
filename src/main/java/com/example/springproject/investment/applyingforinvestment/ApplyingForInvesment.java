package com.example.springproject.investment.applyingforinvestment;

import com.example.springproject.entity.AppUser;
import com.example.springproject.investment.RequestStatus;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ApplyingForInvesment   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column

    private String issue;

    @Size( max = 4000)
     @Column
    private String description;

    private String answer;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus = RequestStatus.IN_PROGRESS;

    private String userName;

    @ManyToOne
    private AppUser rsponsibleUser;

    private Long userFriendly;

    private LocalDate addDate;
    private LocalDate responseDate;

}
