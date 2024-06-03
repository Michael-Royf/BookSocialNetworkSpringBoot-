package com.michael.book_social_network.payload.request;

import com.michael.book_social_network.utility.validationPassword.PasswordMatches;
import com.michael.book_social_network.utility.validationPassword.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@PasswordMatches
public class RegistrationRequest {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @ValidPassword
    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Confirmation password is mandatory")
    private String confirmationPassword;

}
