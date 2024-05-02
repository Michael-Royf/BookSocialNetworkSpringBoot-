package com.michael.book_social_network.service.impl;

import com.michael.book_social_network.entity.Book;
import com.michael.book_social_network.entity.Feedback;
import com.michael.book_social_network.entity.User;
import com.michael.book_social_network.exceptions.payload.OperationNotPermittedException;
import com.michael.book_social_network.payload.request.FeedBackRequest;
import com.michael.book_social_network.payload.response.FeedbackResponse;
import com.michael.book_social_network.payload.response.PageResponse;
import com.michael.book_social_network.repository.BookRepository;
import com.michael.book_social_network.repository.FeedBackRepository;
import com.michael.book_social_network.service.FeedbackService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedBackRepository feedBackRepository;
    private final BookRepository bookRepository;
    private final ModelMapper mapper;


    @Override
    public Long createFeedback(FeedBackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + request.getBookId()));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You cannot give a feedback for and archived or not shareable book");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot give feedback to your own book");
        }
        Feedback feedback = Feedback.builder()
                .book(book)
                .comment(request.getComment())
                .note(request.getNote())
                .build();
        return feedBackRepository.save(feedback).getId();
    }

    @Override
    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Long bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedBackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                //.map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .map(feedback -> mapper.map(feedback, FeedbackResponse.class))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
//6.12