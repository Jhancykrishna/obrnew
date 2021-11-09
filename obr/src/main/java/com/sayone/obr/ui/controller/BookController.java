package com.sayone.obr.ui.controller;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/home")
    public String home(){
         return "welcome to book page";
     }

//get all books
    @GetMapping("books")
    public List<BookEntity>getBooks(){

        return bookService.getBooks();

    }
//get book by id
    @GetMapping("/books/{bId}")
    public Optional<BookEntity> getBook(@PathVariable String bId){
        return bookService.getBook(Long.parseLong(bId));

    }
//post a book
    @PostMapping("/book")
    public BookEntity addBook(@RequestBody BookEntity books) {
        return bookService.addBook(books);
    }
//update a book
    @PutMapping("/books")
    public BookEntity updateBook(@RequestBody BookEntity books) {
        return this.bookService.updateBook(books);
    }

    @DeleteMapping("/books/{bId}")
    public ResponseEntity<HttpStatus>deleteBook(@PathVariable String bId){
        try{
            bookService.deleteBook(Long.parseLong(bId));
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
