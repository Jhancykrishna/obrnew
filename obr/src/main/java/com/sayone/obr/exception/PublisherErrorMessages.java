package com.sayone.obr.exception;

public enum PublisherErrorMessages {

    MISSING_REQUIRED_FIELD("Missing Required field. Please check documentation for required fields"),
    INTERNAL_SERVER_ERROR("internal error.Please debug."),
    NO_RECORD_FOUND("no record found.Please debug."),
    INVALID_ADDRESS("There is no address with this ID"),
    NO_REVIEW_ID_FOUND("There is no Review for this publisher"),
    NO_PUBLISHER_FOUND("Please login as a Publisher"),
    COULD_NOT_UPDATE_RECORD("could not update record.Please debug."),
    COULD_NOT_DELETE_RECORD("could not delete record.Please debug."),
    RECORD_ALREADY_EXISTS("Record already exists"),
    REVIEW_ALREADY_GIVEN("User has Already given review for the Publisher"),
    CANT_DOWNLOAD_BOOK("Upgrade to Prime User"),
    SWITCH_TO_USER("Login as a user to Download the Book"),
    NO_BOOK_FOUND("There is no such Book exist"),
    DELETED_ACCOUNT("Account is deleted");

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
