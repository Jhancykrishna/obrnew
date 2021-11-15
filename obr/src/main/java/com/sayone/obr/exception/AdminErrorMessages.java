package com.sayone.obr.exception;

public enum AdminErrorMessages {

    MISSING_REQUIRED_FIELD("Missing Required field. Please check documentation for required fields"),
    INTERNAL_SERVER_ERROR("internal error.Please debug."),
    NO_RECORD_FOUND("no record found.Please debug."),
    INVALID_ADDRESS("There is no address with this ID"),
    NO_REVIEW_ID_FOUND("There is no Review for this Admin"),
    NO_ADMIN_FOUND("Please login as an Admin"),
    COULD_NOT_UPDATE_RECORD("could not update record.Please debug."),
    COULD_NOT_DELETE_RECORD("could not delete record.Please debug."),
    RECORD_ALREADY_EXISTS("Record already exists"),
    REVIEW_ALREADY_GIVEN("User has Already given review for the Admin"),
    DELETED_ACCOUNT("Account is deleted");

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
