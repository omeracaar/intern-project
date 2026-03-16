package com.omeracar.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book extends BaseEntity {

    private String bookName;

    private String author;

    private int pageCount;

    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}
