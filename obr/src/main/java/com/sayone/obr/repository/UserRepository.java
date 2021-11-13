package com.sayone.obr.repository;

import com.sayone.obr.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository <UserEntity,Long>{
    UserEntity findByEmail(String email);

    @Query(value = "select * from user u where u.user_id = ?1 and u.role = ?2", nativeQuery = true)
    UserEntity findByPublisherId(String userId, String role);

    @Query(value = "select * from user u where u.role = ?1", nativeQuery = true)
    UserEntity findAllByRole(String role);

    @Query(value = "select * from user u where u.id = ?1", nativeQuery = true)
    UserEntity findAllById(Long id);
}
