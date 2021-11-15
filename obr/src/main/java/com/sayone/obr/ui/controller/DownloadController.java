package com.sayone.obr.ui.controller;


import com.sayone.obr.dto.UserDto;
//import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.service.DownloadService;
import com.sayone.obr.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
//import org.springframework.web.multipart.MultipartFile;

//import java.io.IOException;

@RestController
@RequestMapping("user")
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @Autowired
    UserService userService;

//    @Autowired
//    BookEntity bookEntity;

    @PostMapping("/download/{bid}")
    public String createDownload(@PathVariable(value = "bid") Long bookId) throws MessagingException, UnsupportedEncodingException {
        UserEntity userEntity = new UserEntity();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        BeanUtils.copyProperties(user,userEntity);
        downloadService.downloadBook(user,bookId);
        System.out.println("haI "+user.getFirstName()+user.getLastName());
        return "Thank you" + user.getFirstName()+ user.getLastName()+ " " + "Your book is downloaded successfully";
    }

    @GetMapping("/download/getDno/{bid}")
    public String getDownloadNumber(@PathVariable(value = "bid") Long bookId) {
        UserEntity userEntity = new UserEntity();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        BeanUtils.copyProperties(user,userEntity);
        downloadService.getDownloadNumber1(user,bookId);
        System.out.println("Hi "+user.getFirstName()+user.getLastName());
        String returnValue = downloadService.getDownloadNumber1(user,bookId);
        return  returnValue;
    }

//    @PostMapping("book/mail/{bid}")
//    public void mailTest(@PathVariable(value = "bid") String bookId) throws IOException, MessagingException {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserDto user = userService.getUser(auth.getName());
//        downloadService.mailBook(bookId,user.getUserId());
//    }
//

//    @PostMapping("/book/upload/{bid}")
//    public void uploadBook(@RequestParam("file") MultipartFile file,
//                           @PathVariable(value = "bid") String bookId ) throws IOException {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserDto user = userService.getUser(auth.getName());
//        downloadService.uploadBook(file,bookId,user.getUserId());
//    }

}

