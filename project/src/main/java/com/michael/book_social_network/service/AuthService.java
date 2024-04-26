package com.michael.book_social_network.service;

import com.michael.book_social_network.payload.request.AuthenticationRequest;
import com.michael.book_social_network.payload.request.RegistrationRequest;
import com.michael.book_social_network.payload.response.AuthenticationResponse;
import jakarta.mail.MessagingException;

public interface AuthService {

    void register(RegistrationRequest request) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void activateAccount(String token) throws MessagingException;
}
