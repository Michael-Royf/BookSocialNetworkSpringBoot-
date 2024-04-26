package com.michael.book_social_network.exceptions.payload;

public class UserAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
