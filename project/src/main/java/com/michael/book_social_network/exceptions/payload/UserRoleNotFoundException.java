package com.michael.book_social_network.exceptions.payload;

public class UserRoleNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserRoleNotFoundException(String message) {
        super(message);
    }
}
