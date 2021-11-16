package com.sayone.obr.exception;

public enum DownloadErrors {
    MISSING_REQUIRED_FIELD("Missing Required field. Please check documentation for required fields"),
    INTERNAL_SERVER_ERROR("internal error.Please debug"),
    NO_RECORD_FOUND("no record found.Please debug"),
    AUTHENTICATION_FAILED("Authentication Failed"),
    RECORD_ALREADY_EXISTS("Record already exists"),
    COULD_NOT_UPDATE_RECORD("Could not update record"),
    COULD_NOT_DELETE_RECORD("Could not delete record"),
    EMAIL_NOT_VERIFIED("Email not verified"),
    INVALID_RATING("Invalid Rating"),
    NO_BOOK_FOUND("No such book exist!"),
    DOWNLOAD_LIMIT("Your downloads of this book become exceeds.. Contact Admin for further details.."),
    GET_PRIME_ACCOUNT("Sorry You can't download this book. You need to get a prime membership for downloading this book. To get a membership contact admin"),
    PUBLISHER_CANT_DOWNLOAD("Sorry publisher can't download this please contact admin...");




    private String errorMessage;

    DownloadErrors(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
