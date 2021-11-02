package com.sayone.obr.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "books")
public class BookEntity implements Serializable {

    private static final long serialVersionUID = 85931244509056697L;

    @Id
    @GeneratedValue
    private long Id;

    @Column(nullable = false)
    private String bookId;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(nullable = false)
    private String bookName;

    @Column(nullable = false)
    private String authorName;

    @Column(nullable = false)
    private String status;

}
