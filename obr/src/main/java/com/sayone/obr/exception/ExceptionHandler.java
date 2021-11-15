package com.sayone.obr.exception;



import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserServiceException.class)
    public ResponseEntity <Object> handleUserServiceException(UserServiceException ex, WebRequest request)
    {
        return new ResponseEntity<>(ex.getMessage(),new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
