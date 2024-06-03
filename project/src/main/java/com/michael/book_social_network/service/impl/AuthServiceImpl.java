package com.michael.book_social_network.service.impl;

import com.michael.book_social_network.entity.User;
import com.michael.book_social_network.entity.VerificationToken;
import com.michael.book_social_network.enumeration.EmailTemplateName;
import com.michael.book_social_network.exceptions.payload.EmailExistException;
import com.michael.book_social_network.exceptions.payload.UserNotFoundException;
import com.michael.book_social_network.exceptions.payload.UserRoleNotFoundException;
import com.michael.book_social_network.exceptions.payload.UsernameExistException;
import com.michael.book_social_network.payload.request.AuthenticationRequest;
import com.michael.book_social_network.payload.request.RegistrationRequest;
import com.michael.book_social_network.payload.response.AuthenticationResponse;
import com.michael.book_social_network.repository.RoleRepository;
import com.michael.book_social_network.repository.UserRepository;
import com.michael.book_social_network.security.JwtService;
import com.michael.book_social_network.service.AuthService;
import com.michael.book_social_network.service.EmailService;
import com.michael.book_social_network.service.VerificationTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.michael.book_social_network.constant.UserConstant.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Override
    public void register(RegistrationRequest request) throws MessagingException {
        validateNewUsernameAndEmail(
                StringUtils.EMPTY,
                request.getUsername().trim(),
                request.getEmail().trim().toLowerCase());

        String roleName = "USER";
        var userRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new UserRoleNotFoundException(String.format("Role with name: %s not found", roleName)));

        User user = User.builder()
                .username(request.getUsername())
                .firstName(firstLetterUpper(request.getFirstName()))
                .lastName(firstLetterUpper(request.getLastName()))
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);

        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = verificationTokenService.generateAndSaveVerificationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );
        User user = ((User) auth.getPrincipal());

        var claims = new HashMap<String, Object>();

        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .JWT_token(jwtToken)
                .build();
    }


    //@Transactional
    @Override
    public void activateAccount(String token) throws MessagingException {
        VerificationToken savedToken = verificationTokenService.findByToken(token);
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to the same email address");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        verificationTokenService.saveToken(savedToken);
    }

    private void validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findOptionalUserByUsername(newUsername).orElse(null);
        User userByNewEmail = userRepository.findUserByEmail(newEmail).orElse(null);
        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findOptionalUserByUsername(currentUsername).orElse(null);
            if (currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
        } else {
            if (userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
        }
    }

    private Optional<User> findOptionalUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }


    private String firstLetterUpper(String world) {
        String[] words = world.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                char firstChar = Character.toUpperCase(word.charAt(0));
                String capitalizedWord = firstChar + word.substring(1).toLowerCase();
                result.append(capitalizedWord).append(" ");
            }
        }
        return result.toString().trim();
    }

}
