package com.michael.book_social_network.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookRequest {
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    @NotBlank(message = "Author name is mandatory")
    @Size(min = 1, max = 50, message = "Author name must be between 1 and 50 characters")
    private String authorName;

    @NotBlank(message = "ISBN is mandatory")
    //  @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "ISBN should be valid")
    private String isbn;

    @NotBlank(message = "Synopsis is mandatory")
    @Size(min = 10, max = 2000, message = "Synopsis must be between 10 and 2000 characters")
    private String synopsis;

    private boolean shareable;
}
