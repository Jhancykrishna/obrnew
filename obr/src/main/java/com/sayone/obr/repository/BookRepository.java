package com.sayone.obr.repository;
import com.sayone.obr.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

  //for upload book
  @Query(value = "SELECT * FROM book b where b.book_id=?1", nativeQuery = true)

  Optional<BookEntity> findUploadArea(Long bookId) ;
  //to delete
  @Query(value = "SELECT * FROM book b where b.book_id=?1", nativeQuery = true)

  BookEntity findById(Long bId, String userId);

  //to delete book
  @Query(value = "SELECT * FROM book b where b.book_id=?1", nativeQuery = true)
  Optional<BookEntity> findByDeleteArea(Long bookId);


  //BookEntity findById(long bookId);

  //to delete book
  @Query(value = "SELECT * FROM book b where b.book_id=?1", nativeQuery = true)
  Optional<BookEntity> findByDeleteArea(Long bookId);

  //for upload book
  @Query(value = "SELECT * FROM book b where b.book_id=?1", nativeQuery = true)
  Optional<BookEntity> findUploadArea(Long bookId) ;

}
