package com.fullstackbackend.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(Integer id){
        super("Could not found request with id "+ id);
    }
}
