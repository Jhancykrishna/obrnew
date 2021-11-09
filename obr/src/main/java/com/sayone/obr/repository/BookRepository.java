package com.sayone.obr.repository;
import com.sayone.obr.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
  BookEntity findById(long bookId);

}
