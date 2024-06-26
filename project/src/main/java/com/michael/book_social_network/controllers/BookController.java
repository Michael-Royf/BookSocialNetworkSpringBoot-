package com.michael.book_social_network.controllers;

import com.michael.book_social_network.payload.request.BookRequest;
import com.michael.book_social_network.payload.response.BookResponse;
import com.michael.book_social_network.payload.response.BorrowedBookResponse;
import com.michael.book_social_network.payload.response.PageResponse;
import com.michael.book_social_network.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Long> createBook(@RequestBody @Valid BookRequest book,
                                                      Authentication connectedUser) {
        return new ResponseEntity<>(bookService.createBook(book, connectedUser), CREATED);
    }

    @GetMapping("/{book_id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book_id") Long bookId) {
        return new ResponseEntity<>(bookService.getBookById(bookId), OK);
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                   @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                                                   Authentication connectUser) {
        return new ResponseEntity<>(bookService.findAllBooks(page, size, connectUser), OK);
    }


    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllByOwner(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                     @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                                                     Authentication connectUser) {
        return new ResponseEntity<>(bookService.findAllBooksByOwner(page, size, connectUser), OK);
    }


    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                                   @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                                                                   Authentication connectUser) {
        return new ResponseEntity<>(bookService.findAllBorrowedBooks(page, size, connectUser), OK);
    }


    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                                   @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                                                                   Authentication connectUser) {
        return new ResponseEntity<>(bookService.findAllReturnedBooks(page, size, connectUser), OK);
    }


    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Long> updateShareableStatus(@PathVariable("book-id") Long bookId,
                                                                 Authentication authentication) {
        return new ResponseEntity<>(bookService.updateShareableStatus(bookId, authentication), OK);
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Long> updateArchivedStatus(@PathVariable("book-id") Long bookId,
                                                                Authentication authentication) {
        return new ResponseEntity<>(bookService.updateArchivedStatus(bookId, authentication), OK);
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Long> borrowBook(@PathVariable("book-id") Long bookId,
                                                      Authentication authentication) {
        return new ResponseEntity<>(bookService.borrowBook(bookId, authentication), OK);
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Long> returnBook(@PathVariable("book-id") Long bookId,
                                                      Authentication authentication) {
        return new ResponseEntity<>(bookService.returnBorrowedBook(bookId, authentication), OK);
    }

    @PatchMapping("/borrow/return//approve/{book-id}")
    public ResponseEntity<Long> approveReturnBorrowBook(@PathVariable("book-id") Long bookId,
                                                                   Authentication authentication) {
        return new ResponseEntity<>(bookService.approveReturnBorrowedBook(bookId, authentication), OK);
    }

    @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(@PathVariable("book-id") Long bookId,
                                                    @Parameter()
                                                    @RequestPart("file")MultipartFile file,
                                                    Authentication connectedUser){
        bookService.uploadBookCoverPicture(bookId, file, connectedUser);
        return ResponseEntity.accepted().build();

    }
}

//11.04