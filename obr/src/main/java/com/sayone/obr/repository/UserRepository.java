package com.sayone.obr.repository;

import com.sayone.obr.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    //publisher
    @Query(value = "select * from user u where u.user_id = ?1 and u.role = ?2", nativeQuery = true)
    UserEntity findByPublisherId(String userId, String role);

    @Query(value = "select * from user u where u.id = ?1 and u.role = ?2", nativeQuery = true)
    UserEntity findById(Long id, String role);

    @Query(value = "select * from user u where u.role = ?1", nativeQuery = true)
    List<UserEntity> findAllByRole(String role);

    @Query(value = "select * from user u where u.id = ?1", nativeQuery = true)
    UserEntity findAllById(Long id);

    @Query(value = "select u.first_name,u.email,u.phone_number,u.address,b.book_name,b.author,b.year_of_publication from user u left join book b on u.id = :id and u.id = b.publisher_id", nativeQuery = true)
    List<Object[]> findAllBooks(@Param("id")Long id);

    //user
    UserEntity findByEmail(String email);

    UserEntity getUserByUserId(String userId);

    Optional<UserEntity> findByUserId(String userId);


    Optional<UserEntity> findById(Long Id);

    void deleteByUserId(String userId);

    @Query(value = "SELECT id,first_name as firstName,last_name as lastName,email,role, created_on as createdOn FROM user WHERE created_on >= ?1 ORDER BY id", nativeQuery = true)
    List<Map<String, Object>> newUsers(String s);
}