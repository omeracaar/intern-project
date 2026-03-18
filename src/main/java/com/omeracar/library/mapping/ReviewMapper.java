package com.omeracar.library.mapping;

import com.omeracar.library.dto.request.ReviewRequestDto;
import com.omeracar.library.dto.response.ReviewResponseDto;
import com.omeracar.library.entity.Review;

public class ReviewMapper {

    private ReviewMapper (){} //new engellemek için private constructor

    public static ReviewResponseDto toDto(Review review){

        if (review == null){
            return null;
        }

        //review en dış ilişki olduğu için diğer mapperlardaki gibi list e bir alt ilişkiyi eklememe gerek yok
        //o yüzden direkt dtoya çeviriyorum
        return new ReviewResponseDto(
                review.getId(),
                review.getContent(),
                review.getRating(),
                review.getBook() != null ? review.getBook().getBookName() : "Book info not found"
        );
    }

}
