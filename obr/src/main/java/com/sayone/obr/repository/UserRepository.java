package com.sayone.obr.repository;

import com.sayone.obr.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository <UserEntity,Long>{
    UserEntity findByEmail(String email);

    @Query(value = "select * from user u where u.user_id = ?1 and u.publisher = ?2", nativeQuery = true)
    UserEntity findByPublisherId(String userId, String publisher);
}
