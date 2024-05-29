package com.michael.book_social_network.payload.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "First name should not be empty")
    @NotBlank
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    @NotBlank
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email is not formatted")
    @NotBlank
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @NotBlank
    private String password;


}
