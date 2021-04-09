package com.qelery.TaskRestApi.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("Could not find Category with id " + id);
    }
}