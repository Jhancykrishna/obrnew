package com.sayone.obr.exception;

public enum AdminErrorMessages {

    MISSING_REQUIRED_FIELD("Missing Required field. Please check documentation for required fields"),
    INTERNAL_SERVER_ERROR("internal error.Please debug."),
    NO_RECORD_FOUND("No record found.Please debug."),
    INVALID_ADDRESS("There is no address with this ID"),
    NO_REVIEW_ID_FOUND("There is no Review for this Admin"),
    NO_ADMIN_FOUND("Please login as an Admin"),
    COULD_NOT_UPDATE_RECORD("Could not update record.Please debug."),
    COULD_NOT_DELETE_RECORD("Could not delete record.Please debug."),
    RECORD_ALREADY_EXISTS("Record already exists"),
    REVIEW_ALREADY_GIVEN("User has Already given review for the Admin"),
    DELETED_ACCOUNT("Account is deleted"),
    DELETED_BOOK("Book is deleted"),
    NO_BOOK_FOUND("No book is available right now..."),
    NOT_AN_ADMIN("You are not an Admin!!! Please login as an Admin"),
    NO_USERS_EXIST("No Users Exist!!!"),
    NO_PUBLISHERS_EXIST("No Publishers Exist!!!"),
    INVALID_LOGIN("Email or Password you entered is Wrong! Please check again.");

    private String adminErrorMessages;

    public String getAdminErrorMessages() {
        return adminErrorMessages;
    }

    public void setAdminErrorMessages(String adminErrorMessages) {
        this.adminErrorMessages = adminErrorMessages;
    }

    AdminErrorMessages(String adminErrorMessages) {
        this.adminErrorMessages = adminErrorMessages;


    }
}
