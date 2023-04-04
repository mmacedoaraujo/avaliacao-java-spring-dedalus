package com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
