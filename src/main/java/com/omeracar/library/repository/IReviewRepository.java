package com.omeracar.library.repository;

import com.omeracar.library.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByBookIdAndRecIsDeletedFalse(Long bookId);
}
