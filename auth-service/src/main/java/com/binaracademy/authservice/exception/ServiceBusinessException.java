package com.binaracademy.authservice.exception;

public class ServiceBusinessException extends RuntimeException{
    public ServiceBusinessException(String message) {
        super(message);
    }
}
