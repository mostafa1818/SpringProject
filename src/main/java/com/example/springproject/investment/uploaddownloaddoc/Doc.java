package com.example.springproject.investment.uploaddownloaddoc;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name="docs")
public class Doc {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String docName;
    private String docType;
    private Date addDate;

    @Lob
    private byte[] data;

    private Long invesmentId;
    private Long userId;

    public Doc() {}

    public Doc(String docName, String docType, byte[] data) {
        super();
        this.docName = docName;
        this.docType = docType;
        this.data = data;
    }
}