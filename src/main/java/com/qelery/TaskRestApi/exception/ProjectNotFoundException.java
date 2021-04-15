package com.qelery.TaskRestApi.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(Long id) {
        super("Could not find Project with id " + id);
    }
}