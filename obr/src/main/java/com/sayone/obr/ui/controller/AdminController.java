package com.sayone.obr.ui.controller;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.exception.AdminErrorMessages;
import com.sayone.obr.exception.DownloadErrors;
import com.sayone.obr.exception.PublisherErrorMessages;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.model.request.AdminDetailsRequestModel;
import com.sayone.obr.model.response.AdminRestModel;
import com.sayone.obr.model.response.PublisherRestModel;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.service.BookService;
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
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @Autowired
    DownloadService downloadService;

    @Autowired
    BookRepository bookRepository;

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${adminController.authorizationHeader.description}", paramType = "header")})
    @GetMapping("/admin/get-publishers")
    public List<UserEntity> getAllPublishers() throws UserServiceException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        if (!Objects.equals(user.getRole(), "admin")) throw new UserServiceException(AdminErrorMessages.NOT_AN_ADMIN.getAdminErrorMessages());

        PublisherRestModel returnValue = new PublisherRestModel();

//        UserDto userDto = userService.getAllPublishersByRole();
//        BeanUtils.copyProperties(userDto, returnValue);

        return userService.getAllPublishersByRole();
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${adminController.authorizationHeader.description}", paramType = "header")})
    @GetMapping("/admin/get-users")
    public List<UserEntity> getAllUsers() throws UserServiceException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        if (!Objects.equals(user.getRole(), "admin")) throw new UserServiceException(AdminErrorMessages.NOT_AN_ADMIN.getAdminErrorMessages());

//        UserRestModel returnValue = new UserRestModel();

        return userService.getAllUsersByRole();
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${adminController.authorizationHeader.description}", paramType = "header")})
    @GetMapping("/admin/get-all")
    public List<UserEntity> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "limit", defaultValue = "1") int limit) throws UserServiceException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        if (!Objects.equals(user.getRole(), "admin")) throw new UserServiceException(AdminErrorMessages.NOT_AN_ADMIN.getAdminErrorMessages());

//        UserRestModel returnValue = new UserRestModel();

        return userService.getAll(page, limit);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${adminController.authorizationHeader.description}", paramType = "header")})
    @PostMapping("admin/download/book/{bid}")
    public String downloadBook(@PathVariable(value = "bid") Long bookId) throws MessagingException, IOException, UserServiceException {
        UserEntity userEntity = new UserEntity();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        BeanUtils.copyProperties(user,userEntity);

        if (!Objects.equals(user.getRole(), "admin")) throw new UserServiceException(DownloadErrors.PUBLISHER_CANT_DOWNLOAD.getErrorMessage());

        BookEntity bookEntity = bookRepository.getBookId(bookId);

        if (bookEntity == null) throw new UserServiceException(AdminErrorMessages.NO_BOOK_FOUND.getAdminErrorMessages());

        downloadService.downloadBook(user,bookId);
        System.out.println("hai "+user.getFirstName()+ " " + user.getLastName());
        return "Thank you " + user.getFirstName() + user.getLastName() + " " + " Your book is downloaded successfully ";
    }

    @PostMapping("admin/signup")
    public AdminRestModel createAdmin(@RequestBody AdminDetailsRequestModel adminDetails) throws UserServiceException {

        AdminRestModel returnValue = new AdminRestModel();

        if (adminDetails.getEmail().isEmpty() || adminDetails.getPassword().isEmpty()) throw new UserServiceException(PublisherErrorMessages.MISSING_REQUIRED_FIELD.getPublisherErrorMessages());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(adminDetails, userDto);

        UserDto createdAdmin = userService.createUser(userDto);
        BeanUtils.copyProperties(createdAdmin, returnValue);

        return returnValue;
    }

//    @PutMapping("/admin/update")
//    public PublisherRestModel updatePublisher(@RequestBody PublisherDetailsRequestModel publisherDetails) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserDto user = userService.getUser(auth.getName());
//
//        PublisherRestModel returnValue = new PublisherRestModel();
//
//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(publisherDetails, userDto);
//
//        UserDto updatedPublisher = userService.updatePublisher(user.getUserId(), userDto);
//        BeanUtils.copyProperties(updatedPublisher, returnValue);
//
//        return returnValue;
//    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${adminController.authorizationHeader.description}", paramType = "header")})
    @DeleteMapping("/admin/del/publisher/{id}")
    public String deletePublisher(@PathVariable Long id) throws UserServiceException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        if (!Objects.equals(user.getRole(), "admin")) throw new UserServiceException(AdminErrorMessages.NOT_AN_ADMIN.getAdminErrorMessages());

        return userService.deletePublisherByAdmin(id);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${adminController.authorizationHeader.description}", paramType = "header")})
    @DeleteMapping("/admin/del/user/{id}")
    public String deleteUser(@PathVariable Long id) throws UserServiceException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        if (!Objects.equals(user.getRole(), "admin")) throw new UserServiceException(AdminErrorMessages.NOT_AN_ADMIN.getAdminErrorMessages());

        return userService.deleteUserByAdmin(id);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${adminController.authorizationHeader.description}", paramType = "header")})
    @DeleteMapping("/admin/del/book/{id}")
    public String deletePostedBook(@PathVariable Long id) throws UserServiceException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        if (!Objects.equals(user.getRole(), "admin")) throw new UserServiceException(AdminErrorMessages.NOT_AN_ADMIN.getAdminErrorMessages());

        return bookService.deletePostedBookByAdmin(id);
    }
}
