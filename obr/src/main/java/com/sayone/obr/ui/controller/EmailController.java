package com.sayone.obr.ui.controller;

import com.sayone.obr.model.request.UserDetailsRequestModel;
import com.sayone.obr.service.EmailService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;

public class EmailController {

    @Autowired
    EmailService emailService;

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${userController.authorizationHeader.description}", paramType = "header")})
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    @ResponseBody
    public String sendMail(@RequestBody UserDetailsRequestModel userDetails) throws MessagingException {
        emailService.sendMail(userDetails);
        return "Email Sent Successfully.!";
    }
}
