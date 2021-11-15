package com.sayone.obr.dto;

import com.sayone.obr.entity.UserEntity;

import java.io.Serializable;

public class BookDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long bookId;
    private String bookName;
    private String author;
    private String publisher;
    private String genre;
    private String bookStatus;
    private Long yearOfPublication;
    private String bookDescription;
    private UserEntity uid;


    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Long getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(Long yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public UserEntity getUid() {
        return uid;
    }

    public void setUid(UserEntity uid) {
        this.uid = uid;
    }
}
