package com.michael.book_social_network.exceptions.payload;

public class OperationNotPermittedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OperationNotPermittedException(String message) {
        super(message);
    }
}
