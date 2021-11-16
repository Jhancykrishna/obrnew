package com.sayone.obr.repository;

import com.sayone.obr.entity.DownloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DownloadRepository extends JpaRepository<DownloadEntity, Long> {


    @Query(value = "SELECT dno FROM downloads d where d.uid=?1 and d.bid=?2", nativeQuery = true)
    long getDownloadCheck(String userId, Long bookId);


    @Query(value = "select * from downloads d where d.uid = ?1 and d.book_id = ?2",nativeQuery = true)
    Optional<DownloadEntity> findByUserId(String userId, Long bid);

    @Query(value = "select book_link from book b where b.book_id = ?1",nativeQuery = true)
    String findBooksLink(Long bookId);

    @Query(value = "select book_name from book b where b.book_id = ?1",nativeQuery = true)
    String findBookName(Long bookId);

    @Query(value = "select book_status from book b where b.book_id = ?1",nativeQuery = true)
    String findByBookStatus(Long bookId);


//
//    @Query(value = "SELECT * FROM downloads d where d.uid=?2 and d.bid=?1", nativeQuery = true)
//    Optional<DownloadEntity> findUploadArea(String bookId, String userId);
//
//    @Query(value = "SELECT * FROM downloads d where d.uid=?2 and d.bid=?1", nativeQuery = true)
//    Optional<DownloadEntity> findByDeleteArea(String bookId, String userId);
}
