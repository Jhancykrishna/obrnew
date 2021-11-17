package com.sayone.obr.service.impl;

import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.exception.PublisherErrorMessages;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.UserRepository;
import com.sayone.obr.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Component

public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    public BookServiceImpl(){

    }
//get all books
    @Override
    public List<BookEntity> getBooks() {
        List<BookEntity> list=(List<BookEntity>)this.bookRepository.findAll();
        return list;
    }

    @Override
    public Optional<BookEntity> getBook(Long bId) {

        return bookRepository.findByBookId(bId);
    }

    @Override
    public BookEntity addBook(BookEntity books, Long id) throws Exception {
        UserEntity userEntity = userRepository.findAllById(id);
        if (userEntity == null || !Objects.equals(userEntity.getRole(), "publisher")) throw new Exception(PublisherErrorMessages.NO_PUBLISHER_FOUND.getPublisherErrorMessages());
        Optional<BookEntity>bookEntity=bookRepository.searchBooks(books.getBookName(),id);
        if(!bookEntity.isEmpty()) {throw new Exception(PublisherErrorMessages.BOOK_ALREADY_PRESENT.getPublisherErrorMessages());}


        books.setUid(userRepository.getById(id));

        return bookRepository.save(books);
    }


    @Override
    public void deleteBook(Long bId, Long id) throws Exception {
        BookEntity bookEntity = bookRepository.findAllByIds(bId, id);

        if (bookEntity == null) throw new Exception(PublisherErrorMessages.COULD_NOT_DELETE_RECORD.getPublisherErrorMessages());

        BookEntity entity = bookRepository.getById(bId);
        bookRepository.delete(entity);


    }

    @Override
    public BookEntity updateBook(BookEntity books,Long bId, Long id) throws Exception {

        BookEntity bookEntity = bookRepository.findAllByIds(bId, id);

        if (bookEntity == null) throw new Exception(PublisherErrorMessages.COULD_NOT_UPDATE_RECORD.getPublisherErrorMessages());
        bookEntity.setBookName(books.getBookName());
        bookEntity.setAuthor(books.getAuthor());
        bookEntity.setBookStatus(books.getBookStatus());
        bookEntity.setGenre(books.getGenre());
        bookEntity.setPublisher(books.getPublisher());
        bookEntity.setYearOfPublication(books.getYearOfPublication());
        return bookRepository.save(bookEntity);
        //return bookEntity;
    }

    @Override
    public void uploadBook(MultipartFile file, Long bookId, Long id) throws IOException {
        Optional<BookEntity> optionalUpload = bookRepository.findUploadArea(bookId,id);
        if(!optionalUpload.isPresent()) {throw new IOException(PublisherErrorMessages.NOT_YOUR_BOOK.getPublisherErrorMessages());}
        BookEntity findUpload = optionalUpload.get();

        if (optionalUpload.isEmpty()) {
            throw new IllegalStateException("No book found to upload..");
        }
        else {
            String path = "/home/meenakshi/obrnew/obr/BookUpload/" + bookId + ".pdf";
            file.transferTo(new File(path));
            findUpload.setBookLink(path);
            System.out.println("Book PDF Uploaded Successfully.....");
            bookRepository.save(findUpload);
        }


    }

    @Override
    public void deleteBookUpload(Long bookId, Long id) throws IOException {

        BookEntity bookEntity = bookRepository.findAllByIds(bookId, id);

        if (bookEntity == null) throw new IOException(PublisherErrorMessages.COULD_NOT_DELETE_RECORD.getPublisherErrorMessages());

        Optional<BookEntity> optionalDelete = bookRepository.findByDeleteArea(bookId);
        BookEntity findDelete = optionalDelete.get();
        String pathCheck = findDelete.getBookLink();
        if (Objects.equals(pathCheck, "") || Objects.equals(pathCheck, "deleted..")) {
            System.out.println("No files to delete");
        } else {
            Path path = Path.of ("//home/meenakshi/obrnew/obr/BookUpload/" + bookId + ".pdf");
            Files.delete((Path) path);
            findDelete.setBookLink("deleted..");
            System.out.println("Book PDF deleted successfully");
            bookRepository.save(findDelete);
        }
    }

    @Override
    public void deletePostedBookByAdmin(Long bId) throws Exception {

        BookEntity bookEntity = bookRepository.findAllByBookId(bId);

        if (bookEntity == null) throw new Exception(PublisherErrorMessages.COULD_NOT_DELETE_RECORD.getPublisherErrorMessages());

        BookEntity entity = bookRepository.getById(bId);
        bookRepository.delete(entity);
    }
}
