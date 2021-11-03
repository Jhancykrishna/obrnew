package com.sayone.obr.repository;

import com.sayone.obr.entity.DownloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DownloadRepository extends JpaRepository<DownloadEntity, Long> {


    @Query(value = "SELECT dno FROM downloads d where d.uid=?1 and d.bid=?2", nativeQuery = true)

    long getDownloadCheck(String userId, String bookId);

//    "select * from cart c where c.user_id = ?1 and c.cart_status = ?2",

    @Query(value = "select * from downloads d where d.uid = ?1",nativeQuery = true)
    Optional<DownloadEntity> findByUserId(String userId);


    @Query(value = "SELECT * FROM downloads d where d.uid=?2 and d.bid=?1", nativeQuery = true)
    Optional<DownloadEntity> findUploadArea(String bookId, String userId);
}
