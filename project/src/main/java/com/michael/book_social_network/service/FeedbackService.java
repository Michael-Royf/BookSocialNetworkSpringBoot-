package com.michael.book_social_network.service;

import com.michael.book_social_network.payload.request.FeedBackRequest;
import com.michael.book_social_network.payload.response.FeedbackResponse;
import com.michael.book_social_network.payload.response.PageResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    Long createFeedback(FeedBackRequest request, Authentication connectedUser);


    PageResponse<FeedbackResponse> findAllFeedbacksByBook(Long bookId, int page, int size, Authentication connectedUser);
}
