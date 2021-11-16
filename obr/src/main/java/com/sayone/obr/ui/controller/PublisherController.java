package com.sayone.obr.ui.controller;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.exception.AdminErrorMessages;
import com.sayone.obr.exception.PublisherErrorMessages;
import com.sayone.obr.model.request.PublisherDetailsRequestModel;
import com.sayone.obr.model.response.PublisherRestModel;
import com.sayone.obr.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping
public class PublisherController {

    @Autowired
    UserService userService;

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${publisherController.authorizationHeader.description}", paramType = "header")})
    @GetMapping("/publisher/get")
    public PublisherRestModel getPublisher() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        if (!Objects.equals(user.getRole(), "publisher")) throw new Exception(PublisherErrorMessages.NO_PUBLISHER_FOUND.getPublisherErrorMessages());

        PublisherRestModel returnValue = new PublisherRestModel();

        UserDto userDto = userService.getPublisherById(user.getUserId());
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping("/publisher/signup")
    public PublisherRestModel createPublisher(@RequestBody PublisherDetailsRequestModel publisherDetails) throws Exception {

        PublisherRestModel returnValue = new PublisherRestModel();

        if (publisherDetails.getFirstName().isEmpty()) throw new Exception(PublisherErrorMessages.MISSING_REQUIRED_FIELD.getPublisherErrorMessages());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(publisherDetails, userDto);

        UserDto createdPublisher = userService.createUser(userDto);
        BeanUtils.copyProperties(createdPublisher, returnValue);

        return returnValue;
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${publisherController.authorizationHeader.description}", paramType = "header")})
    @PutMapping("/publisher/update")
    public PublisherRestModel updatePublisher(@RequestBody PublisherDetailsRequestModel publisherDetails) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        if (!Objects.equals(user.getRole(), "publisher")) throw new Exception(PublisherErrorMessages.NO_PUBLISHER_FOUND.getPublisherErrorMessages());


        PublisherRestModel returnValue = new PublisherRestModel();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(publisherDetails, userDto);

        UserDto updatedPublisher = userService.updatePublisher(user.getUserId(), userDto);
        BeanUtils.copyProperties(updatedPublisher, returnValue);

        return returnValue;
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${publisherController.authorizationHeader.description}", paramType = "header")})
    @DeleteMapping("/publisher/delete")
    public void deletePublisher() throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        if (!Objects.equals(user.getRole(), "publisher")) throw new Exception(PublisherErrorMessages.NO_PUBLISHER_FOUND.getPublisherErrorMessages());

        userService.deletePublisher(user.getUserId());
    }
}
