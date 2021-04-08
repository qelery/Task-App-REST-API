package com.qelery.TaskRestApi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class NotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String CategoryNotFoundHandler(CategoryNotFoundException ex)  {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String TaskNotFoundHandler(TaskNotFoundException ex)  {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CategoryExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String CategoryExistsHandler(CategoryExistsException ex)  {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UsernameExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String UsernameExistsHandler(UsernameExistsException ex)  {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(EmailAddressExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String EmailAddressExistsHandler(EmailAddressExistsException ex)  {
        return ex.getMessage();
    }
}