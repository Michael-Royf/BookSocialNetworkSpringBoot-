package com.michael.book_social_network.service;

import com.michael.book_social_network.enumeration.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmail(String to,
                   String username,
                   EmailTemplateName emailTemplateName,
                   String confirmationUrl,
                   String activationCode,
                   String subject) throws MessagingException;
}
