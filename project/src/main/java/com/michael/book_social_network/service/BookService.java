package com.michael.book_social_network.service;

import com.michael.book_social_network.payload.request.BookRequest;
import com.michael.book_social_network.payload.response.BookResponse;
import com.michael.book_social_network.payload.response.BorrowedBookResponse;
import com.michael.book_social_network.payload.response.PageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;


public interface BookService {
    Long createBook(BookRequest bookRequest, Authentication connectedUser);

    BookResponse getBookById(Long bookId);

    PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectUser);

    PageResponse<BookResponse>  findAllBooksByOwner(int page, int size, Authentication connectUser);

    PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectUser);

    PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectUser);

    Long updateShareableStatus(Long bookId, Authentication authentication);

    Long updateArchivedStatus(Long bookId, Authentication authentication);

    Long borrowBook(Long bookId, Authentication authentication);

    Long returnBorrowedBook(Long bookId, Authentication authentication);

    Long approveReturnBorrowedBook(Long bookId, Authentication authentication);

    void uploadBookCoverPicture(Long bookId, MultipartFile file, Authentication connectedUser);
}
