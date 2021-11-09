package com.sayone.obr.ui.controller;


import com.sayone.obr.dto.UserDto;
import com.sayone.obr.model.request.UserDetailsRequestModel;
import com.sayone.obr.model.response.UserRestModel;
import com.sayone.obr.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public String getUser()
    {
        return "get user called";
    }

    @PostMapping("users/signup")
    public UserRestModel createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRestModel returnValue = new UserRestModel();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping("/users/update")
    public String updateUser()
    {
        return "update user called";
    }


    @DeleteMapping
    public String deleteUser()
    {
        return "delete user called";
    }

}
