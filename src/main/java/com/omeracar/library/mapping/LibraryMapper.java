package com.omeracar.library.mapping;

import com.omeracar.library.dto.response.LibraryResponseDto;
import com.omeracar.library.entity.Library;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryMapper {

    private LibraryMapper() {} //new engellemek için private constructor

    public static LibraryResponseDto toDto(Library library) {

        //library boş gelirse dtosu da null dönsün
        if (library == null) {
            return null;
        }

        List<String> bookNames = library.getBooks() != null
                ? library.getBooks().stream()
                .filter(book -> !book.isRecIsDeleted()) //soft delete yapılanları getirmemesi için
                .map(book -> book.getBookName() + " " + book.getAuthor() + " " + book.getPageCount())
                .toList()
                : Collections.emptyList();

        return new LibraryResponseDto(
                library.getId(),
                library.getName(),
                library.getLocation(),
                library.getPhoneNumber(),
                library.getUpdatedAt(),
                bookNames
        );
    }

}
