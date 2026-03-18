package com.omeracar.library.controller;

import com.omeracar.library.dto.request.ReviewRequestDto;
import com.omeracar.library.dto.response.ReviewResponseDto;
import com.omeracar.library.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto requestDto){
        return reviewService.createReview(requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByBookId(@PathVariable Long id){
        return reviewService.getReviewsByBookId(id);
    }

}
