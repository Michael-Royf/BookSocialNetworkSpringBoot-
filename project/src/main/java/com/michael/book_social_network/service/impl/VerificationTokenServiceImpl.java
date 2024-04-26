package com.michael.book_social_network.service.impl;

import com.michael.book_social_network.entity.VerificationToken;
import com.michael.book_social_network.entity.User;
import com.michael.book_social_network.repository.VerificationTokenRepository;
import com.michael.book_social_network.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public String generateAndSaveVerificationToken(User user) {
        String generatedToken = generateActivationCode(6);
        var token = VerificationToken.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        verificationTokenRepository.save(token);
        return generatedToken;
    }

    @Override
    public void saveToken(VerificationToken token) {
        verificationTokenRepository.save(token);
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
