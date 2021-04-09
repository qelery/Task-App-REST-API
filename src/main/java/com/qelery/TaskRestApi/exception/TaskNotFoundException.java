package com.qelery.TaskRestApi.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Could not find Task with id " + id);
    }
}