package com.Maktab101.SpringProject.utils.exceptions;

public class DuplicateValueException extends RuntimeException {
    public DuplicateValueException(String value) {
        super("The value '"+value+"' already exists in database");
    }
}
