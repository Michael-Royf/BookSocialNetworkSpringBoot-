package com.michael.book_social_network.exceptions.payload;


public class BookNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BookNotFoundException(String message) {
        super(message);
    }
}
