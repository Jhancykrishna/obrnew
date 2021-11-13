package com.sayone.obr.entity;

import javax.persistence.*;

@Entity
@Table(name = "book")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bookId;
    @Column(nullable = false)
    private String bookName;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String publisher;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private String bookStatus;
    @Column
    private String bookLink;
    @Column(nullable = false)
    private long yearOfPublication;
    @Column(nullable = false)
    private String bookDescription;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity uid;

    public BookEntity() {

        super();
    }

    public BookEntity(long bookId, String bookName, String author, String publisher, String genre, String bookStatus, String bookLink, long yearOfPublication, String bookDescription, UserEntity uid) {
        this.bookId= bookId;
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.genre = genre;
        this.bookStatus = bookStatus;
        this.bookLink = bookLink;
        this.yearOfPublication = yearOfPublication;
        this.bookDescription = bookDescription;
        this.uid = uid;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
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

    public void setYearOfPublication(long yearOfPublication) {
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
