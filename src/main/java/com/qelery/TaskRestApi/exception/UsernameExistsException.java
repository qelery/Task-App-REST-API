package com.qelery.TaskRestApi.exception;

public class UsernameExistsException extends RuntimeException {

    public UsernameExistsException(String username) {
        super("User already exists with the username " + username);
    }
}