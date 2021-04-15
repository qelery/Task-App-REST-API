package com.qelery.TaskRestApi.exception;

public class ProjectExistsException extends RuntimeException {

    public ProjectExistsException(String name) {
        super("Project already exists with name " + name);
    }
}