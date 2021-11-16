package com.sayone.obr.ui.controller;


import com.sayone.obr.dto.UserDto;
//import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.exception.DownloadErrors;
import com.sayone.obr.exception.PublisherErrorMessages;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.service.DownloadService;
import com.sayone.obr.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
//import org.springframework.web.multipart.MultipartFile;

//import java.io.IOException;

@RestController
@RequestMapping("user")
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @Autowired
    UserService userService;


    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${downloadController.authorizationHeader.description}", paramType = "header")})

    @PostMapping("/download/{bid}")
    public String createDownload(@PathVariable(value = "bid") Long bookId) throws MessagingException, IOException {
        UserEntity userEntity = new UserEntity();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        BeanUtils.copyProperties(user,userEntity);

        if (!Objects.equals(user.getRole(), "user")) throw new UserServiceException(DownloadErrors.PUBLISHER_CANT_DOWNLOAD.getErrorMessage());

        downloadService.downloadBook(user,bookId);
        System.out.println("haI "+user.getFirstName()+user.getLastName());
        return "Thank you" + user.getFirstName()+ user.getLastName()+ " " + "Your book is downloaded successfully";
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${downloadController.authorizationHeader.description}", paramType = "header")})

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

