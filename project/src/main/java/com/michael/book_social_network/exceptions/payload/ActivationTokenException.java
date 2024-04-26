package com.michael.book_social_network.exceptions.payload;

public class ActivationTokenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ActivationTokenException(String message) {
        super(message);
    }
}
