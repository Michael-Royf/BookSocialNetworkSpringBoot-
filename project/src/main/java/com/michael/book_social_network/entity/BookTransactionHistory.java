package com.michael.book_social_network.entity;

import com.michael.book_social_network.entity.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
public class BookTransactionHistory  extends BaseEntity {
    @Id
    @SequenceGenerator(name = "bookHistory_sequence", sequenceName = "bookHistory_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookHistory_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private boolean returned;

    private boolean returnApproved;



}
