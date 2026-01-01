package com.akshay.websockettask.Exceptions;


public class NotFoundException extends  RuntimeException{

    public NotFoundException(String message) {
        super("404 not found: "+message);
    }
}
