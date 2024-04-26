package com.michael.book_social_network.repository;

import com.michael.book_social_network.entity.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long> {

    @Query("select history from  BookTransactionHistory  history where history.user.id = :userId")
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Long userId);


    @Query("select history from  BookTransactionHistory  history where history.book.owner.id = :userId")
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Long userId);

    @Query("""
            SELECT
            (COUNT (*) > 0) AS isBorrowed
            FROM BookTransactionHistory bookTransactionHistory
            WHERE bookTransactionHistory.user.id = :userId
            AND bookTransactionHistory.book.id = :bookId
            AND bookTransactionHistory.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(@Param("bookId") Long bookId,
                                    @Param("userId") Long userId);

    @Query("""
            SELECT transaction
            FROM BookTransactionHistory  transaction
            WHERE transaction.user.id = :userId
            AND transaction.book.id = :bookId
            AND transaction.returned = false
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(@Param("bookId") Long bookId,
                                                           @Param("userId") Long userId);

    @Query("""
            SELECT transaction
            FROM BookTransactionHistory  transaction
            WHERE transaction.book.owner.id = :userId
            AND transaction.book.id = :bookId
            AND transaction.returned = true
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(@Param("bookId") Long bookId,
                                                            @Param("userId") Long userId);

}
