package com.omeracar.library.controller;

import com.omeracar.library.dto.request.LibraryRequestDto;
import com.omeracar.library.dto.response.LibraryResponseDto;
import com.omeracar.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/libraries")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    //create
    @PostMapping
    public ResponseEntity<LibraryResponseDto> createLibrary(@RequestBody LibraryRequestDto requestDto){
        return libraryService.createLibrary(requestDto);
    }

    //getAll pageable
    @GetMapping
    public ResponseEntity<Page<LibraryResponseDto>> getAllLibraries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return libraryService.getAllLibraries(null, page, size);
    }

    //getLibrariesByName
    @GetMapping("/search")
    public ResponseEntity<Page<LibraryResponseDto>> getLibrariesByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
    return libraryService.getLibrariesByName(name,page,size);
    }

    //getById
    @GetMapping("/{id}")
    public ResponseEntity<LibraryResponseDto> getById(@PathVariable Long id){
        return libraryService.getLibraryById(id);
    }

    //update
    @PutMapping("/{id}")
    public ResponseEntity<LibraryResponseDto> updateLibrary(
            @PathVariable Long id,
            @RequestBody LibraryRequestDto requestDto){
        return libraryService.updateLibrary(id,requestDto);
    }

    //soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLibrary(@PathVariable Long id){
        return libraryService.deleteLibrary(id);
    }

    //activate library
    @PutMapping("/{id}/activate")
    public ResponseEntity<LibraryResponseDto> activatedLibrary(@PathVariable Long id){
        return libraryService.activateLibrary(id);
    }

}
