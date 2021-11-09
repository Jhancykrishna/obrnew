package com.sayone.obr.service;

import com.sayone.obr.entity.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookService {
     List<BookEntity> getBooks();
     Optional<BookEntity> getBook(Long bId);
     BookEntity addBook(BookEntity books);
     BookEntity updateBook(BookEntity books);
     void deleteBook(long parseLong);
}
