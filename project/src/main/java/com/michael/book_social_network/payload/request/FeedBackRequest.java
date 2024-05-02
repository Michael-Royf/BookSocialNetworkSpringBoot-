package com.michael.book_social_network.payload.request;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FeedBackRequest {
    @Positive(message = "200")
    @Min(value = 0,message = "201")
    @Max(value = 5, message = "202")
    private Double note;

    @NotNull(message = "203")
    @NotBlank(message = "203")
    @NotEmpty(message = "203")
    private String comment;

    @NotNull(message = "204")
    private Long bookId;
}
