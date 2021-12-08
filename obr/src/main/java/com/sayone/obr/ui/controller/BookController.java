package com.sayone.obr.ui.controller;
import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.exception.PublisherErrorMessages;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.service.BookService;
import com.sayone.obr.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
//book controller
@RestController
@RequestMapping("/")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/home")
    public String home(){
         return "welcome to book page";
     }

//get all books
    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
        value = "${bookController.authorizationHeader.description}", paramType = "header")})
    @GetMapping("books")
    public List<BookEntity>getBooks(){

        return bookService.getBooks();

    }




//get book by id
    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
        value = "${bookController.authorizationHeader.description}", paramType = "header")})
    @GetMapping("/books/{bId}")
    public Optional<BookEntity> getBook(@PathVariable String bId){
        return bookService.getBook(Long.parseLong(bId));

    }




//post a book
    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
        value = "${bookController.authorizationHeader.description}", paramType = "header")})
    @PostMapping("/book")
    public BookEntity addBook(@RequestBody BookEntity books) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        return bookService.addBook(books,user.getId());
    }




//update a book
    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
        value = "${bookController.authorizationHeader.description}", paramType = "header")})
    @PutMapping("/book/{bId}")
    public BookEntity updateBook(@RequestBody BookEntity books,@PathVariable Long bId) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        return this.bookService.updateBook(books,bId, user.getId());
    }






    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${bookController.authorizationHeader.description}", paramType = "header")})
    @DeleteMapping("/books/{bId}")
    public ResponseEntity<HttpStatus>deleteBook(@PathVariable Long bId){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        try{
            bookService.deleteBook(bId, user.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${bookController.authorizationHeader.description}", paramType = "header")})
    @PostMapping("/book/upload/{bid}")
    public void uploadBook(@RequestParam("file") MultipartFile file,
                           @PathVariable(value = "bid") Long bookId ) throws UserServiceException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        if(!file.getContentType().equals("application/pdf")){ throw new UserServiceException(PublisherErrorMessages.ONLY_PDF_FILE_ALLOWED.getPublisherErrorMessages());}
            bookService.uploadBook(file, bookId, user.getId());

    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${bookController.authorizationHeader.description}", paramType = "header")})
    @PostMapping("book/delete/{bid}")
    public void deleteBookUpload(@PathVariable(value = "bid") Long bookId) throws UserServiceException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        bookService.deleteBookUpload(bookId, user.getId());
    }
}
