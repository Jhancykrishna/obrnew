package com.sayone.obr.ui.controller;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.exception.DownloadErrors;
import com.sayone.obr.exception.ErrorMessages;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.model.request.UserDetailsRequestModel;
import com.sayone.obr.service.DownloadService;
import com.sayone.obr.service.EmailService;
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
import java.util.Objects;

@RestController
public class EmailController {

    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;
    @Autowired
    DownloadService downloadService;

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${userController.authorizationHeader.description}", paramType = "header")})
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    @ResponseBody
    public String sendMail(@RequestBody UserDetailsRequestModel userDetails) throws MessagingException {
        emailService.sendMail(userDetails);
        return "Email Sent Successfully.!";
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${downloadController.authorizationHeader.description}", paramType = "header")})

    @PostMapping("/download/{bid}")
    public String createDownload(@PathVariable(value = "bid") Long bookId) throws MessagingException, IOException {
        UserEntity userEntity = new UserEntity();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        BeanUtils.copyProperties(user,userEntity);

        if (!Objects.equals(user.getRole(), "user")) throw new UserServiceException(DownloadErrors.PUBLISHER_CANT_DOWNLOAD.getErrorMessage());

        if (bookId == null) throw new UserServiceException(DownloadErrors.NO_BOOK_FOUND.getErrorMessage());

        try{
            emailService.downloadBook(user,bookId);
            System.out.println("haI "+user.getFirstName()+user.getLastName());
            return "Thank you" + user.getFirstName()+ user.getLastName()+ " " + "Your book is downloaded successfully";

        }
        catch(Exception ex){
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

    }
    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${downloadController.authorizationHeader.description}", paramType = "header")})
    @PostMapping("/downloadAgain/{bid}")
    public String downloadAgain(@PathVariable(value = "bid") Long bookId) throws MessagingException, IOException {
        UserEntity userEntity = new UserEntity();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        BeanUtils.copyProperties(user,userEntity);

        if (!Objects.equals(user.getRole(), "user")) throw new UserServiceException(DownloadErrors.PUBLISHER_CANT_DOWNLOAD.getErrorMessage());

        if (bookId == null) throw new UserServiceException(DownloadErrors.NO_BOOK_FOUND.getErrorMessage());

        try{
            emailService.downloadBookAgain(user,bookId);
            return "Thank you" + user.getFirstName()+ user.getLastName()+ " " + "Your book is downloaded again successfully";

        }
        catch(Exception ex){
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

    }
    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${downloadController.authorizationHeader.description}", paramType = "header")})
    @PostMapping("/outOfDownload/{bid}")
    public String outOfDownload(@PathVariable(value = "bid") Long bookId) throws MessagingException, IOException {
        UserEntity userEntity = new UserEntity();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        BeanUtils.copyProperties(user,userEntity);

        if (!Objects.equals(user.getRole(), "user")) throw new UserServiceException(DownloadErrors.PUBLISHER_CANT_DOWNLOAD.getErrorMessage());

        if (bookId == null) throw new UserServiceException(DownloadErrors.NO_BOOK_FOUND.getErrorMessage());

        try{
            emailService.outOfDownload(user,bookId);
            System.out.println("haI "+user.getFirstName()+user.getLastName());
            return   user.getFirstName()+ user.getLastName()+ " " + "You have 0 downloads left";

        }
        catch(Exception ex){
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

    }



}
