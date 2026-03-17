package com.omeracar.library.mapping;

import com.omeracar.library.dto.response.BookResponseDto;
import com.omeracar.library.entity.Book;
import com.omeracar.library.entity.Review;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {

    private BookMapper (){} //new engellemek için private constructor

    public static BookResponseDto toDto(Book book, Double averageRating){

        //book boş gelirse dtosu da boş dönsün
        if (book == null) {
            return null;
        }

        List<String> comments= book.getReviews() != null
                ? book.getReviews().stream()
                .filter(review -> !review.isRecIsDeleted()) //soft delete yaptıklarımı geitrmiyorum
                .map(Review::getContent)
                .toList()
                : Collections.emptyList();

        return new BookResponseDto(
                book.getId(),
                book.getBookName(),
                book.getAuthor(),
                book.getPageCount(),
                book.getLibrary() != null ? book.getLibrary().getName() : "Not assigned to any library",
                comments,
                averageRating
        );

    }
}
