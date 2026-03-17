package com.omeracar.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {

    private Long id;

    private String bookName;

    private String author;

    private int pageCount;

    private String libraryName; // id yerine direkt kütüphane ismi dondum

    private List<String> reviewComments;

    private Double averageRating;

}
