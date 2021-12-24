package com.sayone.obr.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name= "emailReport")
public class EmailEntity {
    @Id
    @GeneratedValue
    private Long Id;
    private String fromAddress;
    private String toAddress;
    private String subject;
    private LocalDate date;

    public EmailEntity() {
    }

    public EmailEntity(Long id, String fromAddress, String toAddress, String subject, LocalDate date) {
        Id = id;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.subject = subject;
        this.date = date;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
