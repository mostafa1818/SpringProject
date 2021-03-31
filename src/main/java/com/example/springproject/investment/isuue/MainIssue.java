package com.example.springproject.investment.isuue;


import com.example.springproject.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MainIssue  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //i dont know ,it nedds limit or no
      @Column
    private String subjectIssue;
     @Column
    private String detailsIssue;

    @ManyToOne
    @JoinColumn( name="project_Admin")
    private AppUser appUser;

}