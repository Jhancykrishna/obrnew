package com.sayone.obr.repository;
import com.sayone.obr.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_PASS_DISTINCT_THROUGH;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

  //to delete and update book details
  @Query(value = "SELECT * FROM book b where b.book_id=?1 and b.publisher_id=?2", nativeQuery = true)
  BookEntity findAllByIds(Long bId, Long id);

  //to delete uploaded book
  @Query(value = "SELECT * FROM book b where b.book_id=?1", nativeQuery = true)
  Optional<BookEntity> findByDeleteArea(Long bookId);

  //for upload book
  @Query(value = "SELECT * FROM book b where b.book_id=?1", nativeQuery = true)
  Optional<BookEntity> findUploadArea(Long bookId) ;

  @Query(value = "SELECT * FROM book b where b.book_id=?1", nativeQuery = true)
  Optional<BookEntity> findByBookId(Long bId);

  @Query(value = "SELECT * FROM book b where b.book_id=?1", nativeQuery = true)
  BookEntity findAllByBookId(Long bId);
}