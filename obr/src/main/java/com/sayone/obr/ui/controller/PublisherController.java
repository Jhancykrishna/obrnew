package com.sayone.obr.ui.controller;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.exception.PublisherErrorMessages;
import com.sayone.obr.model.request.PublisherDetailsRequestModel;
import com.sayone.obr.model.response.PublisherRestModel;
import com.sayone.obr.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PublisherController {

    @Autowired
    UserService userService;

    @GetMapping("/publisher/get")
    public PublisherRestModel getPublisher() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        PublisherRestModel returnValue = new PublisherRestModel();

        UserDto userDto = userService.getPublisherById(user.getUserId());
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping("publisher/signup")
    public PublisherRestModel createPublisher(@RequestBody PublisherDetailsRequestModel publisherDetails) throws Exception {

        PublisherRestModel returnValue = new PublisherRestModel();

        if (publisherDetails.getFirstName().isEmpty()) throw new Exception(PublisherErrorMessages.MISSING_REQUIRED_FIELD.getPublisherErrorMessages());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(publisherDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping("/publisher/update")
    public PublisherRestModel updatePublisher(@RequestBody PublisherDetailsRequestModel publisherDetails) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        PublisherRestModel returnValue = new PublisherRestModel();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(publisherDetails, userDto);

        UserDto updatedPublisher = userService.updatePublisher(user.getUserId(), userDto);
        BeanUtils.copyProperties(updatedPublisher, returnValue);

        return returnValue;
    }

    @DeleteMapping("/publisher/delete")
    public void deletePublisher() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        userService.deletePublisher(user.getUserId());
    }
}
