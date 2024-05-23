package com.michael.book_social_network.controllers;

import com.michael.book_social_network.payload.request.FeedBackRequest;
import com.michael.book_social_network.payload.response.FeedbackResponse;
import com.michael.book_social_network.payload.response.PageResponse;
import com.michael.book_social_network.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/api/v1/feedbacks")
@RequiredArgsConstructor
@Tag(name = "FeedBack")
public class FeedBackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Long> saveFeedback(@Valid @RequestBody FeedBackRequest request,
                                             Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.createFeedback(request, connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbacksByBook(@PathVariable("book-id") Long bookId,
                                                                                 @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                                 @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                                                                 Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.findAllFeedbacksByBook(bookId, page, size, connectedUser));
    }

 //6:33
}
