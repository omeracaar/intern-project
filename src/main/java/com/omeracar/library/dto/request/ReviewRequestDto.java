package com.omeracar.library.dto.request;

import lombok.Data;

@Data
public class ReviewRequestDto {

    private String content;

    private Double rating;

    private Long bookId; //yorum yapılan kitap
}