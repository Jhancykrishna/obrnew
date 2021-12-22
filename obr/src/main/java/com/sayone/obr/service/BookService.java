package com.sayone.obr.service;

import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.exception.UserServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BookService {
     List<BookEntity> getBooks();
     Optional<BookEntity> getBook(Long bId);
     BookEntity addBook(BookEntity books, Long id) throws UserServiceException;
     BookEntity updateBook(BookEntity books,Long bId, Long id) throws Exception;
     void deleteBook(Long bId, Long id) throws UserServiceException;
     void uploadBook(MultipartFile file, Long bookId, Long id) throws IOException;
     void deleteBookUpload(Long bookId, Long id) throws UserServiceException, IOException;

    String deletePostedBookByAdmin(Long bId) throws UserServiceException;
}
