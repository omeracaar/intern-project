package com.omeracar.library.dto.response;

import lombok.Data;

@Data
public class ReviewResponseDto {

    private Long id;

    private String firstName;

    private String content;

    private int rating;

    private String bookName;
}