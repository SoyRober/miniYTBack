package com.miniyt.exception;

public class AlreadyExistentEntityException extends RuntimeException {
    public AlreadyExistentEntityException (String s) {
        super(s);
    }
}
