package com.sayone.obr.exception;

public enum PublisherErrorMessages {

    MISSING_REQUIRED_FIELD("Missing Required field. Please check documentation for required fields"),
    INTERNAL_SERVER_ERROR("Internal error.Please debug."),
    NO_RECORD_FOUND("No record found.Please debug."),
    INVALID_ADDRESS("There is no address with this ID"),
    NO_REVIEW_ID_FOUND("There is no Review for this publisher"),
    NO_PUBLISHER_FOUND("Please login as a Publisher"),
    COULD_NOT_UPDATE_RECORD("Could not update record.Please debug."),
    COULD_NOT_DELETE_RECORD("Could not delete record.Please debug."),
    RECORD_ALREADY_EXISTS("Record already exists"),
    REVIEW_ALREADY_GIVEN("User has Already given review for the Publisher"),
    CANT_DOWNLOAD_BOOK("Upgrade to Prime User"),
    SWITCH_TO_USER("Login as a user to Download the Book"),
    NO_BOOK_FOUND("There is no such Book exist"),
    DELETED_ACCOUNT("Account is deleted"),
    NOT_YOUR_BOOK("This is not your book, Please enter the correct Book ID or Token value "),
    BOOK_ALREADY_PRESENT("This Book is already Present"),
    MISSING_FIRST_NAME("Fill your first name."),
    MISSING_LAST_NAME("Fill your last name."),
    MISSING_PHONE_NO("Fill your phone number."),
    MISSING_ADDRESS("Fill your address."),
    ONLY_PDF_FILE_ALLOWED("Only PDF File allowed ");

    private String publisherErrorMessages;

    PublisherErrorMessages(String publisherErrorMessages) {
        this.publisherErrorMessages = publisherErrorMessages;
    }

    public String getPublisherErrorMessages() {
        return publisherErrorMessages;
    }

    public void setPublisherErrorMessages(String publisherErrorMessages) {
        this.publisherErrorMessages = publisherErrorMessages;
    }

}
