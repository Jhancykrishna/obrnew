package com.sayone.obr.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "downloads")
public class DownloadEntity implements Serializable {

    private static final long serialVersionUID = -8985931244509056697L;

    @Id
    @GeneratedValue
    private long Id;



    private String uid;

    private long dno;

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    private long bookId;


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
