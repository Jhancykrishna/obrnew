package com.sayone.obr.repository;

import com.sayone.obr.entity.DownloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
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

    @Query(value = "select * from downloads d where d.uid = ?1 and d.book_id = ?2",nativeQuery = true)
    DownloadEntity findByUserAndBookId(String userId, Long bookId);

    @Query(value = "SELECT b.book_name as bookName ,d.dno as dno,u.first_name as firstName FROM `downloads` d INNER JOIN user u ON d.uid = u.user_id INNER JOIN book b ON d.book_id = b.book_id WHERE d.dno = ( SELECT MAX(d.dno) FROM downloads d)",nativeQuery = true)
    List<Map<String,Object>> mostDownload();


//
//    @Query(value = "SELECT * FROM downloads d where d.uid=?2 and d.bid=?1", nativeQuery = true)
//    Optional<DownloadEntity> findUploadArea(String bookId, String userId);
//
//    @Query(value = "SELECT * FROM downloads d where d.uid=?2 and d.bid=?1", nativeQuery = true)
//    Optional<DownloadEntity> findByDeleteArea(String bookId, String userId);
}