package com.omeracar.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review extends BaseEntity{

    private String firstName;

    private String content;

    private int rating;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}
