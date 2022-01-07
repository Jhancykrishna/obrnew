package com.sayone.obr.ui.controller;

import com.sayone.obr.model.request.UserDetailsRequestModel;
import com.sayone.obr.service.DownloadService;
import com.sayone.obr.service.Email;
import com.sayone.obr.service.EmailTimeZone;
import com.sayone.obr.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
public class EmailController {

    @Autowired
    Email email;
    @Autowired
    UserService userService;
    @Autowired
    DownloadService downloadService;
    @Autowired
    EmailTimeZone emailTimeZone;

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${userController.authorizationHeader.description}", paramType = "header")})
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    @ResponseBody
    public String sendMail(@RequestBody UserDetailsRequestModel userDetails) throws MessagingException {
//        email.sendMail(userDetails);
        return "Email Sent Successfully.!";
    }
    @PostMapping("/timezone/{id}")
    public String sendMail1(@PathVariable("id")Long id) throws MessagingException {
        emailTimeZone.sendMail(id);
        return "Email Sent Successfully.!";
    }
}
