package com.sayone.obr.exception;

public class UserServiceException extends RuntimeException{
    public UserServiceException(String message) {
        super(String.valueOf(message));
    }
}
