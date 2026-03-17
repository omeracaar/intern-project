package com.omeracar.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {

    private Long id;

    private String firstName;

    private String content;

    private int rating;

    private String bookName;
}