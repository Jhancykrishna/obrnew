package com.sayone.obr.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "downloads")
public class DownloadEntity implements Serializable {

    private static final long serialVersionUID = -8985931244509056697L;

    @Id
    @GeneratedValue
    private long Id;

    private String bid;

    private String uid;

    private long dno;

    private String bookLink;

    public String getBookLink() {
        return bookLink;
    }

    public void setBookLink(String bookLink) {
        this.bookLink = bookLink;
    }



    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getDno() {
        return dno;
    }

    public void setDno(long dno) {
        this.dno = dno;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }



}
