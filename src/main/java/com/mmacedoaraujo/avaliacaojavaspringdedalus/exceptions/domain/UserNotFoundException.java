package com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions.domain;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
