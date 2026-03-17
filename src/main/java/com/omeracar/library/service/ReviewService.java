package com.omeracar.library.service;

import com.omeracar.library.dto.request.ReviewRequestDto;
import com.omeracar.library.dto.response.ReviewResponseDto;
import com.omeracar.library.entity.Book;
import com.omeracar.library.entity.Review;
import com.omeracar.library.exception.ResourceNotFoundException;
import com.omeracar.library.repository.IBookRepository;
import com.omeracar.library.repository.ILibraryRepository;
import com.omeracar.library.repository.IReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final IReviewRepository reviewRepository;
    private final IBookRepository bookRepository;

    public ResponseEntity<ReviewResponseDto> createReview(ReviewRequestDto requestDto){
        log.info("Creating review for book ID: {}",requestDto.getBookId());

        Book book=bookRepository.findByIdAndRecIsDeletedFalse(requestDto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: "+requestDto.getBookId()));

        Review review=Review.builder()
                .content(requestDto.getContent())
                .rating(requestDto.getRating())
                .book(book)
                .build();

        reviewRepository.save(review);

        return null;
        //return ResponseEntity.status(HttpStatus.CREATED).body(Review);
    }

}
