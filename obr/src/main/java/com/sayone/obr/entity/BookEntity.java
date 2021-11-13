package com.sayone.obr.entity;

import javax.persistence.*;

//import javax.persistence.Column;
@Entity
@Table(name = "book")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;
    private String bookName;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity uid;
  //  @Column(nullable = false)
    private String author;
 //   private String userId;
    private String publisher;
    private String genre;
    private String bookStatus;
    private String bookLink;
    private Long yearOfPublication;
    private  String bookDescription;
    public BookEntity(Long bookId, String bookName, String uid, String author, String publisher, String genre, String bookStatus, String bookLink, Long yearOfPublication, String bookDescription) {
        this.bookId= bookId;
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.genre = genre;
        this.bookStatus = bookStatus;
        this.bookLink = bookLink;
        this.yearOfPublication = yearOfPublication;
        this.bookDescription = bookDescription;
    }


    public BookEntity() {
    }

    public UserEntity getUid() {
        return uid;
    }

    public void setUid(UserEntity uid) {
        this.uid = uid;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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

    public String getBookLink() {
        return bookLink;
    }

    public void setBookLink(String bookLink) {
        this.bookLink = bookLink;
    }

    public long getYearOfPublication() {
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

    @Override
    public String toString() {
        return "BookEntity{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", genre='" + genre + '\'' +
                ", bookStatus='" + bookStatus + '\'' +
                ", bookLink='" + bookLink + '\'' +
                ", yearOfPublication=" + yearOfPublication +
                ", bookDescription='" + bookDescription + '\'' +
                '}';
    }
}
