package com.omeracar.library.repository;

import com.omeracar.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBookRepository extends JpaRepository<Book,Long> {
    Page<Book> findByRecIsDeletedFalse(Pageable pageable);

    Optional<Book> findByIdAndRecIsDeletedFalse(Long id);

    Page<Book> findByBookNameContainingIgnoreCaseAndRecIsDeletedFalse(String bookName, Pageable pageable);
}
