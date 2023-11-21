package com.binaracademy.shoppingservice.exception;

public class DataConflictException extends RuntimeException {
    public DataConflictException(String message){
        super(message);
    }
}
