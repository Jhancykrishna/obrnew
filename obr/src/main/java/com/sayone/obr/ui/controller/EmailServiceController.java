package com.sayone.obr.ui.controller;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.exception.DownloadErrors;
import com.sayone.obr.exception.ErrorMessages;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.model.request.User;
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
@RequestMapping("/email")
public class EmailServiceController {

    @Autowired
    EmailService emailservice;
    @Autowired
    UserService userService;
    @Autowired
    DownloadService downloadService;


    @PostMapping(value = "/send")
    public String sendMail(@RequestBody User user)throws MessagingException {

        emailservice.sendMail(user);
        return "Email Sent Successfully.!";
    }
    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${downloadController.authorizationHeader.description}", paramType = "header")})
    @PostMapping("/download/{bid}")
    public String createDownload(@PathVariable(value = "bid") Long bookId) throws MessagingException, IOException {
        UserEntity userEntity = new UserEntity();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        BeanUtils.copyProperties(user, userEntity);

        if (!Objects.equals(user.getRole(), "user"))
            throw new UserServiceException(DownloadErrors.PUBLISHER_CANT_DOWNLOAD.getErrorMessage());
        try {
            emailservice.downloadBook(user, bookId);
            return "Thank you" + user.getFirstName() + user.getLastName() + " " + "Your book is downloaded successfully";
        } catch (Exception ex) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
    }
}
