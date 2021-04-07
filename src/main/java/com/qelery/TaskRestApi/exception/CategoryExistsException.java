package com.qelery.TaskRestApi.exception;

public class CategoryExistsException extends RuntimeException {

    public CategoryExistsException(String name) {
        super("Category already exists with the name " + name);
    }
}