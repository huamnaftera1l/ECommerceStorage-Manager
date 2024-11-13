package com.jd.exception;

public class ShelfNotEmptyException extends RuntimeException{
    public ShelfNotEmptyException(String message) {
        super(message);
    }
}
