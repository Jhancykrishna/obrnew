package com.sayone.obr.ui.controller;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.exception.ErrorMessages;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.model.request.UserDetailsRequestModel;
import com.sayone.obr.model.response.UserRestModel;
import com.sayone.obr.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController

public class UserController {

    @Autowired
    UserService userService;


    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${userController.authorizationHeader.description}", paramType = "header")})
    @GetMapping("users/get")
    public UserRestModel getUser() {

        UserRestModel returnValue = new UserRestModel();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        UserDto userDto = userService.getUserByUserId(user.getUserId());
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;


    }


    @PostMapping("users/signup")
    public UserRestModel createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRestModel returnValue = new UserRestModel();

        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());


        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }


    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${userController.authorizationHeader.description}", paramType = "header")})
    @PutMapping("users/update")
    public UserRestModel updateUser(@RequestBody UserDetailsRequestModel userDetails) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        UserRestModel returnValue = new UserRestModel();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updateUser = userService.updateUser(user.getUserId(),userDto);
        BeanUtils.copyProperties(updateUser, returnValue);
        return returnValue;
    }


    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${userController.authorizationHeader.description}", paramType = "header")})
    @Transactional
    @DeleteMapping("users/delete")
    public String deleteUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        userService.deleteUserById(user.getUserId());
        return "User Deleted Successfully";
    }

}
