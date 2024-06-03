package com.michael.book_social_network.exceptions.payload;

public class UsernameExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UsernameExistException(String message) {
        super(message);
    }
}
