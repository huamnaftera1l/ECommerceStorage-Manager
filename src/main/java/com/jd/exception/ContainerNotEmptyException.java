package com.jd.exception;

public class ContainerNotEmptyException extends RuntimeException{
    public ContainerNotEmptyException(String message){
        super(message);
    }
}
