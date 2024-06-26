package com.michael.book_social_network.service.impl;

import com.michael.book_social_network.entity.Book;
import com.michael.book_social_network.entity.BookTransactionHistory;
import com.michael.book_social_network.entity.User;
import com.michael.book_social_network.exceptions.payload.OperationNotPermittedException;
import com.michael.book_social_network.payload.request.BookRequest;
import com.michael.book_social_network.payload.response.BookResponse;
import com.michael.book_social_network.payload.response.BorrowedBookResponse;
import com.michael.book_social_network.payload.response.PageResponse;
import com.michael.book_social_network.repository.BookRepository;
import com.michael.book_social_network.repository.BookTransactionHistoryRepository;
import com.michael.book_social_network.service.BookService;
import com.michael.book_social_network.service.FileStorageService;
import com.michael.book_social_network.utility.BookSpecification;
import com.michael.book_social_network.utility.mapper.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository transactionHistoryRepository;
    private final FileStorageService fileStorageService;
    private final BookMapper bookMapper;


    @Override
    public Long createBook(BookRequest bookRequest, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = Book.builder()
                .title(bookRequest.getTitle())
                .authorName(bookRequest.getAuthorName())
                .isbn(bookRequest.getIsbn())
                .synopsis(bookRequest.getSynopsis())
                .shareable(true)
                .owner(user)
                .build();

        return bookRepository.save(book).getId();

    }

    @Override
    public BookResponse getBookById(Long bookId) {
        Book book = findBookInDBById(bookId);
        return bookMapper.toBookResponse(book);
    }

    @Override
    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectUser) {
        User user = (User) connectUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponseList = books
                .stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponseList,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectUser) {
        User user = (User) connectUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);

        List<BookResponse> bookResponseList = books
                .stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponseList,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectUser) {
        User user = (User) connectUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());

        List<BorrowedBookResponse> books = allBorrowedBooks
                .stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                books,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }


    @Override
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectUser) {
        User user = (User) connectUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository
                .findAllReturnedBooks(pageable, user.getId());

        List<BorrowedBookResponse> books = allBorrowedBooks
                .stream()
                .map(bookMapper::toBorrowedBookResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                books,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    @Override
    public Long updateShareableStatus(Long bookId, Authentication authentication) {
        Book book = findBookInDBById(bookId);
        User user = (User) authentication.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot update books shareable status");
        }
        book.setShareable(!book.isShareable());
        return bookRepository.save(book).getId();

    }

    @Override
    public Long updateArchivedStatus(Long bookId, Authentication authentication) {
        Book book = findBookInDBById(bookId);
        User user = (User) authentication.getPrincipal();
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot update other books archived status");
        }
        book.setArchived(!book.isArchived());
        return bookRepository.save(book).getId();
    }

    @Override
    public Long borrowBook(Long bookId, Authentication authentication) {
        Book book = findBookInDBById(bookId);
        User user = (User) authentication.getPrincipal();
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }
        final boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested book is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    @Override
    public Long returnBorrowedBook(Long bookId, Authentication authentication) {
        Book book = findBookInDBById(bookId);
        User user = (User) authentication.getPrincipal();
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow or return  your own book");
        }
        BookTransactionHistory bookTransactionHistory =
                transactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                        .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));
        bookTransactionHistory.setReturned(true);
        return transactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    @Override
    public Long approveReturnBorrowedBook(Long bookId, Authentication authentication) {
        Book book = findBookInDBById(bookId);
        User user = (User) authentication.getPrincipal();
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot return a book that you do not own");
        }
        BookTransactionHistory bookTransactionHistory =
                transactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
                        .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet. You cannot approve its return"));
        bookTransactionHistory.setReturnApproved(true);
        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    @Override
    public void uploadBookCoverPicture(Long bookId,
                                       MultipartFile file,
                                       Authentication connectedUser) {
        Book book = findBookInDBById(bookId);
        User user = (User) connectedUser.getPrincipal();

        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }


    private Book findBookInDBById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Book with id: %s not found", bookId)));
    }


//    public User getLoggedInUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        return findUserByUsernameInDB(username);
//    }


}


//10.52