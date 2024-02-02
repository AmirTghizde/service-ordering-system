package com.Maktab101.SpringProject.utils.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public class ApiException {
    private final String exceptionMascot;
    private final String exceptionType;
    private final String message;
    private final HttpStatus httpStatus;
}
