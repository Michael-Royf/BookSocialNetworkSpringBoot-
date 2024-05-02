package com.michael.book_social_network.service;

import com.michael.book_social_network.payload.request.BookRequest;
import com.michael.book_social_network.payload.response.BookResponse;
import com.michael.book_social_network.payload.response.BorrowedBookResponse;
import com.michael.book_social_network.payload.response.MessageResponse;
import com.michael.book_social_network.payload.response.PageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;


public interface BookService {
    MessageResponse createBook(BookRequest bookRequest, Authentication connectedUser);

    BookResponse getBookById(Long bookId);

    PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectUser);

    PageResponse<BookResponse>  findAllBooksByOwner(int page, int size, Authentication connectUser);


    PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectUser);

    PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectUser);

    MessageResponse updateShareableStatus(Long bookId, Authentication authentication);

    MessageResponse updateArchivedStatus(Long bookId, Authentication authentication);

    MessageResponse borrowBook(Long bookId, Authentication authentication);

    MessageResponse returnBorrowedBook(Long bookId, Authentication authentication);

    MessageResponse approveReturnBorrowedBook(Long bookId, Authentication authentication);

    void uploadBookCoverPicture(Long bookId, MultipartFile file, Authentication connectedUser);
}
