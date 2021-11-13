package com.sayone.obr.ui.controller;
import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.service.BookService;
import com.sayone.obr.service.PublisherService;
import com.sayone.obr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class BookController {

    @Autowired
    BookService bookService;
    @Autowired
    UserService userService;

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
    public Optional<BookEntity> getBook(@PathVariable Long bId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        return bookService.getBook(bId);


    }
//post a book
    @PostMapping("/book")
    public BookEntity addBook(@RequestBody BookEntity books){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        return bookService.addBook(books);
    }
//update a book
    @PutMapping("/books")
    public BookEntity updateBook(@RequestBody BookEntity books) {
        return this.bookService.updateBook(books);
    }


//delete a book

    @DeleteMapping("/books/{bId}")
    public ResponseEntity<HttpStatus>deleteBook(@PathVariable Long bId ){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
            try{
                bookService.deleteBook(bId);
                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

    }
    @PostMapping("/book/upload/{bid}")
    public void uploadBook(@RequestParam("file") MultipartFile file,
                           @PathVariable(value = "bid") Long bookId ) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        bookService.uploadBook(file,bookId);
    }

    @PostMapping("book/delete/{bid}")
    public void deleteBookUpload(@PathVariable(value = "bid") Long bookId) throws IOException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        bookService.deleteBookUpload(bookId);
    }

}
