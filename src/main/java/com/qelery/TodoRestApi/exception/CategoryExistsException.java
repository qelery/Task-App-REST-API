package com.qelery.TodoRestApi.exception;

public class CategoryExistsException extends RuntimeException {

    public CategoryExistsException(String name) {
        super("Category already exists with the name " + name);
    }
}