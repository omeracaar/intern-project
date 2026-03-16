package com.omeracar.library.dto.request;

import lombok.Data;

@Data
public class BookRequestDto {

    private String name;

    private String author;

    private int pageCount;

    private Long libraryId; // hangi kutuphaneye eklenecegi
}
