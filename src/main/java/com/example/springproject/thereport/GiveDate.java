package com.example.springproject.thereport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class GiveDate {
    Integer startYear;
    Integer endYear;
    Integer startmonth;
    Integer endmonth;
    Integer startday;
    Integer endday;
}
