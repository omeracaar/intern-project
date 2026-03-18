package com.omeracar.library.service;

import com.omeracar.library.dto.request.BookRequestDto;
import com.omeracar.library.dto.response.BookResponseDto;
import com.omeracar.library.entity.Book;
import com.omeracar.library.entity.Library;
import com.omeracar.library.entity.Review;
import com.omeracar.library.exception.ResourceNotFoundException;
import com.omeracar.library.mapping.BookMapper;
import com.omeracar.library.repository.IBookRepository;
import com.omeracar.library.repository.ILibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final IBookRepository bookRepository;
    private final ILibraryRepository libraryRepository;

    //create
    public ResponseEntity<BookResponseDto> createBook(BookRequestDto requestDto) {
        log.info("Creating new book, name is: {} for library ID: {}", requestDto.getBookName(), requestDto.getLibraryId());

        Library library = libraryRepository.findByIdAndRecIsDeletedFalse(requestDto.getLibraryId())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found with ID: " + requestDto.getLibraryId()));

        Book book = Book.builder()
                .bookName(requestDto.getBookName())
                .author(requestDto.getAuthor())
                .pageCount(requestDto.getPageCount())
                .library(library) // üst classla ilişki
                .build();

        //örnek kodda burada bendeki review mantığına denk gelen hobby vardı fakat kitap eklendiği gibi review edilmiş
        //olamaycağı için review kısmına ayrı bir servis yazacağım
        Book savedBook = bookRepository.save(book);

        return ResponseEntity.status(HttpStatus.CREATED).body(BookMapper.toDto(savedBook, 0.0));
    }

    //ortalama rating veren method
    private Double calculateAverageRating(List<Review> reviews) {
        //eğer review yok ise 0 döndüm
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .filter(review -> !review.isRecIsDeleted()) //silinmiş yorumları hesaba katmadım
                .mapToDouble(Review::getRating)
                .average()//ortalama
                .orElse(0.0);
    }

    //pageable
    public ResponseEntity<Page<BookResponseDto>> getAllBooks(int page, int size) {
        log.info("Fetching all active books");

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> bookPage = bookRepository.findByRecIsDeletedFalse(pageable);

        if (bookPage == null || !bookPage.hasContent()) {
            throw new ResourceNotFoundException("Book not found");
        }

        //her kitabı dtoya çevirmeden önce üstte yazdığım metottan ortalamasını alıp öyle çevirdim
        Page<BookResponseDto> dtoPage = bookPage.map(book -> {
            Double avg = calculateAverageRating(book.getReviews());
            return BookMapper.toDto(book, avg);
        });

        return ResponseEntity.ok(dtoPage);
    }

    //get by id
    public ResponseEntity<BookResponseDto> getBookById(Long id) {
        log.info("Fetching book with ID: {}", id);

        Book book = bookRepository.findByIdAndRecIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        //ortalama puan
        Double avgRating = calculateAverageRating(book.getReviews());

        return ResponseEntity.ok(BookMapper.toDto(book, avgRating));
    }

    //update
    public ResponseEntity<BookResponseDto> updateBook(Long id, BookRequestDto requestDto) {
        log.info("Updating book with ID: {}", id);

        Book book = bookRepository.findByIdAndRecIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Library library = libraryRepository.findByIdAndRecIsDeletedFalse(requestDto.getLibraryId())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found"));

        //log önceki değerler
        log.info("Previous book data - bookName: {}, author: {}, pageCount: {}, libraryId: {}",
                book.getBookName(),
                book.getAuthor(),
                book.getPageCount(),
                book.getLibrary() != null ? book.getLibrary().getId() : "null");

        book.setBookName(requestDto.getBookName());
        book.setAuthor(requestDto.getAuthor());
        book.setPageCount(requestDto.getPageCount());
        book.setLibrary(library);

        Book updatedBook = bookRepository.save(book);

        //log sonrası değerler
        log.info("Book with ID {} updated successfully", updatedBook.getId());
        log.info("Updated book data - bookName: {}, author: {}, pageCount: {}, libraryId: {}",
                updatedBook.getBookName(),
                updatedBook.getAuthor(),
                updatedBook.getPageCount(),
                updatedBook.getLibrary().getId());

        Double averageRating = calculateAverageRating(updatedBook.getReviews());
        return ResponseEntity.ok(BookMapper.toDto(updatedBook, averageRating));
    }

    //soft delete
    public ResponseEntity<String> deleteBook(Long id) {
        log.info("Deleting book with ID: {}", id);

        Book book = bookRepository.findByIdAndRecIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        book.setRecIsDeleted(true);

        if (book.getReviews() != null) {
            book.getReviews().forEach(review -> review.setRecIsDeleted(true));
        }

        bookRepository.save(book);
        return ResponseEntity.ok("Book deleted successfully");
    }

    //activate
    //library silinmişse o kitabı etkinleştiremeyiz mantığı
    //örnek kodda da aynı mantık var
    public ResponseEntity<BookResponseDto> activateBook(Long id) {
        log.info("Activating book with ID: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        if (!book.isRecIsDeleted()) {
            log.warn("Book with ID {} is already active", id);
            return ResponseEntity.badRequest().body(null);
        }

        if (book.getLibrary() != null && book.getLibrary().isRecIsDeleted()) {
            log.error("Cannot activate book ID {} because its library ID {} is inactive",
                    id, book.getLibrary().getId());
            throw new IllegalStateException("Cannot activate book because the library is inactive");
        }

        book.setRecIsDeleted(false);

        //kitabı aktive ederken eski yorumları da aktive ettim
        if (book.getReviews() != null) {
            book.getReviews().forEach(review -> {
                review.setRecIsDeleted(false);
                log.info("Review ID {} activated with book", review.getId());
            });
        }

        Book updated = bookRepository.save(book);
        Double avgRating = calculateAverageRating(updated.getReviews());

        return ResponseEntity.ok(BookMapper.toDto(updated, avgRating));
    }

    //page
    public ResponseEntity<Page<BookResponseDto>> getBooksByName(String bookName, int page, int size) {
        log.info("Searching for books containing name: {}. Page: {}, Size: {}", bookName, page, size);

        //isme göre alfabetik sıralama
        Pageable pageable = PageRequest.of(page, size, Sort.by("bookName").ascending());

        Page<Book> books;

        if (bookName == null || bookName.isEmpty()) {
            books = bookRepository.findByRecIsDeletedFalse(pageable);
        } else {
            books = bookRepository.findByBookNameContainingIgnoreCaseAndRecIsDeletedFalse(bookName, pageable);
        }

        //her kitabı dto ya çevirirken ortalama hesaplayıp koydum
        Page<BookResponseDto> dtoPage = books.map(book -> {
            Double avg = calculateAverageRating(book.getReviews());
            return BookMapper.toDto(book, avg);
        });

        return ResponseEntity.ok(dtoPage);
    }

}
