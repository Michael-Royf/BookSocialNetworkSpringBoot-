package com.michael.book_social_network.exceptions.payload;

public class EmailExistException  extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public EmailExistException(String message) {
        super(message);
    }
}
