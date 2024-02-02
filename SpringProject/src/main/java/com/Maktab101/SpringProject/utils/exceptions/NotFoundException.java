package com.Maktab101.SpringProject.utils.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String resourceName) {
        super("The resource '" + resourceName + "' was not found");
    }
}
