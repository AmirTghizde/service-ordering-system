package com.Maktab101.SpringProject.utils.exceptions;

import org.hibernate.QueryException;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;

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
    public ResponseEntity<Object> handelDuplicateValueException(DuplicateValueException e) {
        ApiException apiException = new ApiException(
                "(⚆ᗝ⚆) (☉_ ☉)",
                e.getClass().getSimpleName(),
                e.getMessage(),
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<Object> handelCustomException(CustomException e) {
        ApiException apiException = new ApiException(
                "(╥_╥)",
                e.getClass().getSimpleName(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = QueryException.class)
    public ResponseEntity<Object> handelQueryException(QueryException e) {
        ApiException apiException = new ApiException(
                "(꒪⌓꒪)",
                e.getClass().getSimpleName(),
                e.getMessage(),
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = DateTimeException.class)
    public ResponseEntity<Object> handelDateTimeException(DateTimeException e) {
        ApiException apiException = new ApiException(
                "ミ●﹏☉ミ",
                e.getClass().getSimpleName(),
                e.getMessage(),
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = PathElementException.class)
    public ResponseEntity<Object> handelPathElementExceptionException(PathElementException e) {
        ApiException apiException = new ApiException(
                "ب_ب",
                e.getClass().getSimpleName(),
                e.getMessage(),
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

}
