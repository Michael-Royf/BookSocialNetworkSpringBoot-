package com.michael.book_social_network.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticationRequest {
    //validation
    private String email;
    private String password;
}
