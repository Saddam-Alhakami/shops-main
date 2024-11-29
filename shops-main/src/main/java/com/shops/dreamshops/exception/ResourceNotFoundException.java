package com.shops.dreamshops.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String categoryNotFound) {
        super(categoryNotFound);
    }
}
