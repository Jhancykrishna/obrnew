package com.sayone.obr.exception;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing Required field. Please check documentation for required fields"),
    INTERNAL_SERVER_ERROR("internal error.Please debug"),
    NO_RECORD_FOUND("no record found.Please debug"),
    AUTHENTICATION_FAILED("Authentication Failed"),
    RECORD_ALREADY_EXISTS("Record already exists"),
    COULD_NOT_UPDATE_RECORD("Could not update record"),
    COULD_NOT_DELETE_RECORD("Could not delete record"),
    EMAIL_NOT_VERIFIED("Email not verified"),
    INVALID_RATING("Invalid Rating"),
    NO_BOOK_ID_FOUND("no bookId found");





    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
