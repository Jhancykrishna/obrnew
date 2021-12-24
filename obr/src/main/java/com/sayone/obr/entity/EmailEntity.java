package com.sayone.obr.entity;

import javax.persistence.*;

@Entity
@Table(name = "email")
public class EmailEntity{

    @Id
    @GeneratedValue
    private Long Id;

    private String fromAddress;
    private String toAddress;
    private String subject;
    private String date;

    public EmailEntity() {
    }

    public EmailEntity(Long id, String fromAddress, String toAddress, String subject, String date) {
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

    public String getFromAddress(String fromAddress) {
        return this.fromAddress;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
