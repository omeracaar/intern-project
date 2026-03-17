package com.omeracar.library.service;

import com.omeracar.library.dto.request.BookRequestDto;
import com.omeracar.library.dto.response.BookResponseDto;
import com.omeracar.library.repository.IBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    @Autowired
    private IBookRepository iBookRepository;

    //create
    public BookResponseDto createBook(BookRequestDto dto){
        log.info("Crafting new Book with name: {}",dto.getName());

    }

}
