package com.omeracar.library.dto.request;

import lombok.Data;

@Data
public class ReviewRequestDto {

    private String firstName;

    private String content;

    private int rating;

    private Long bookId; //yorum yapilan kitap
}