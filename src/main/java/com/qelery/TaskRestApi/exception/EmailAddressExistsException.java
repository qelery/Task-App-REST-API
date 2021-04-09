package com.qelery.TaskRestApi.exception;

public class EmailAddressExistsException extends RuntimeException {

    public EmailAddressExistsException(String emailAddress) {
        super("User already exists with email address " + emailAddress);
    }
}