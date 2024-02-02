package com.Maktab101.SpringProject.utils;

import com.Maktab101.SpringProject.utils.exceptions.ApiException;
import com.Maktab101.SpringProject.utils.exceptions.DuplicateValueException;
import com.Maktab101.SpringProject.utils.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handelNotFoundException(NotFoundException e) {
        ApiException apiException = new ApiException(
                "( º﹃º )",
                e.getClass().getSimpleName(),
                e.getMessage(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = DuplicateValueException.class)
    public ResponseEntity<Object> handelNotFoundException(DuplicateValueException e) {
        ApiException apiException = new ApiException(
                "(⚆ᗝ⚆) (☉_ ☉)",
                e.getClass().getSimpleName(),
                e.getMessage(),
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }
}
