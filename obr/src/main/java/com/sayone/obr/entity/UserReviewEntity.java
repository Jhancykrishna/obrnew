package com.sayone.obr.entity;


import javax.persistence.*;

@Entity
@Table(name = "reviews")
public class UserReviewEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reviewId;
    private Integer rating;
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id",referencedColumnName = "bookId")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private BookEntity bookEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "Id")

    private UserEntity userEntity;

    public UserReviewEntity() {
    }

    public UserReviewEntity(Long reviewId, Integer rating, String description, BookEntity bookEntity, UserEntity userEntity) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.description = description;
        this.bookEntity = bookEntity;
        this.userEntity = userEntity;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BookEntity getBookEntity() {
        return bookEntity;
    }

    public void setBookEntity(BookEntity bookEntity) {
        this.bookEntity = bookEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
