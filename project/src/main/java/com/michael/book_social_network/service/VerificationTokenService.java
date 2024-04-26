package com.michael.book_social_network.service;

import com.michael.book_social_network.entity.User;
import com.michael.book_social_network.entity.VerificationToken;

public interface VerificationTokenService {
    String generateAndSaveVerificationToken(User user);

    void saveToken(VerificationToken token);

    VerificationToken findByToken(String token);
}
