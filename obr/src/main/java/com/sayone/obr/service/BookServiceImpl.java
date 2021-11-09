package com.sayone.obr.service;

import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Component

public class BookServiceImpl implements BookService{

    @Autowired
    BookRepository bookRepository;

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

        return bookRepository.findById(bId);
    }

    @Override
    public BookEntity addBook(BookEntity books) {

        return bookRepository.save(books);
    }


    @Override
    public void deleteBook(long parseLong) {

        BookEntity entity = bookRepository.getOne(parseLong);
        bookRepository.delete(entity);


    }

    @Override
    public BookEntity updateBook(BookEntity books) {

        bookRepository.save(books);
        return books;
    }
}
