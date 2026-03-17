package com.omeracar.library.controller;

import com.omeracar.library.dto.request.BookRequestDto;
import com.omeracar.library.dto.response.BookResponseDto;
import com.omeracar.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    //create
    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(@RequestBody BookRequestDto bookRequestDto){
        return bookService.createBook(bookRequestDto);
    }

    //get all
    @GetMapping
    public ResponseEntity<Page<BookResponseDto>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return bookService.getAllBooks(page, size);
    }

    //get
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    //update
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(
            @PathVariable Long id,
            @RequestBody BookRequestDto requestDto) {
        return bookService.updateBook(id, requestDto);
    }

    //soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    //activate
    @PutMapping("/{id}/activate")
    public ResponseEntity<BookResponseDto> activateBook(@PathVariable Long id) {
        return bookService.activateBook(id);
    }

    //search pageable
    @GetMapping("/search")
    public ResponseEntity<Page<BookResponseDto>> getBooksByName(
            @RequestParam(required = false) String bookName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return bookService.getBooksByName(bookName, page, size);
    }

}
