package com.omeracar.library.dto.response;

import lombok.Data;

@Data
public class BookResponseDto {

    private Long id;

    private String name;

    private String author;

    private int pageCount;

    private String libraryName; // id yerine direkt kütüphane ismi dondum
}
