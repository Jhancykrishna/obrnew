package com.sayone.obr.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -8985931244509056697L;

    @Id
    @GeneratedValue
    private Long Id;
    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;
    @Column(length = 50)
    private String firstName;
    @Column(length = 50)
    private String lastName;
    @Column(nullable = false, length = 120, unique = true)
    private String email;
    @Column(nullable = false)
    private String encryptedPassword;
    @Column(length = 25)
    private long phoneNumber;
    @Column(nullable = false)
    private String role;

    @Column(length = 120)
    private String address;

    @Column
    private String userStatus;

    @OneToMany(fetch = FetchType.EAGER,mappedBy="uid",cascade = CascadeType.ALL)
    private Set<BookEntity> book;

    public UserEntity() {

        super();
    }

    public UserEntity(String userId, String firstName, String lastName, String email, String encryptedPassword, long phoneNumber, String role, String address, String userStatus, Set<BookEntity> book) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.address = address;
        this.userStatus = userStatus;
        this.book = book;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<BookEntity> getBook() {
        return book;
    }

    public void setBook(Set<BookEntity> book) {
        this.book = book;
    }
}

