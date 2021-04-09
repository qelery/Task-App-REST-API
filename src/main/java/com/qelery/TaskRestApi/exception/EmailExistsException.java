package com.qelery.TaskRestApi.exception;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String email) {
        super("User already exists with email " + email);
    }
}