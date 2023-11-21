package com.binaracademy.shoppingservice.exception;

public class ServiceBusinessException extends RuntimeException{
    public ServiceBusinessException(String message) {
        super(message);
    }
}
