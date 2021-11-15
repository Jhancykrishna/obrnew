package com.sayone.obr.repository;

import com.sayone.obr.entity.UserReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReviewEntity,Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM reviews r WHERE r.book_id = ?1 and r.user_id = ?2", nativeQuery = true)
    void deleteReview(Long bookId, Long Id);


    @Query(value = "select * from reviews r where r.user_id = ?1", nativeQuery = true)
    List<UserReviewEntity> findByUserId(Long Id);

    @Query(value = "select * from reviews r where r.book_id = ?1 and r.user_id = ?2", nativeQuery = true)
    Optional<UserReviewEntity> findByBookAndUser(Long bookId, Long Id);

    @Query(value = "select * from reviews r where r.book_id = ?1 and r.user_id = ?2", nativeQuery = true)
    Optional<UserReviewEntity> findByBookIdAndUserId(Long bookId, Long Id);


    @Query(value = "select * from reviews r where r.book_id = ?1", nativeQuery = true)
    Optional<UserReviewEntity> getBookId(Long bookId);
}
