package com.binaracademy.commerceservice.exception;

public class DataConflictException extends RuntimeException {
    public DataConflictException(String message){
        super(message);
    }
}
