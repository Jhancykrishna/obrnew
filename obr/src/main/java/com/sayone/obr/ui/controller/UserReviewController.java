package com.sayone.obr.ui.controller;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.model.request.ReviewRequestModel;
import com.sayone.obr.model.response.ReviewResponseModel;
import com.sayone.obr.service.UserReviewService;
import com.sayone.obr.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("review")
public class UserReviewController {

    @Autowired
    UserReviewService userReviewService;

    @Autowired
    UserService userService;

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${userReviewController.authorizationHeader.description}", paramType = "header")})
    @PostMapping("post")
    public ReviewResponseModel createReview(@RequestBody ReviewRequestModel reviewRequestModel) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        ReviewResponseModel reviewResponseModel = userReviewService.createReview(reviewRequestModel,
                user.getId());
        return reviewResponseModel;
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${userReviewController.authorizationHeader.description}", paramType = "header")})
    @GetMapping(path = "get/{bid}")
    public ReviewResponseModel getReviewByBookId(@PathVariable("bid") Long bookId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        ReviewResponseModel reviewResponseModel =  userReviewService.getReviewsByBookId(bookId, user.getId());
        return reviewResponseModel;

    }


    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${userReviewController.authorizationHeader.description}", paramType = "header")})

    @GetMapping("get/list")
    public List<ReviewResponseModel> getAllReview() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        List<ReviewResponseModel> reviewResponseModels = userReviewService.findReviewsByUser(user.getId());
        return reviewResponseModels;

    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${userReviewController.authorizationHeader.description}", paramType = "header")})
    @PutMapping("update")
    public ReviewResponseModel updateReview(@RequestBody ReviewRequestModel reviewRequestModel) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        ReviewResponseModel reviewResponseModel = userReviewService.updateReview(reviewRequestModel, user.getId());
        return reviewResponseModel;
    }


    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${userReviewController.authorizationHeader.description}", paramType = "header")})
    @DeleteMapping("delete/{bid}")
    public String deleteRating(@PathVariable("bid") Long bookId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        userReviewService.deleteReview(bookId, user.getId());
        return "UserReview Deleted Successfully";
    }
}
