package com.omeracar.library.service;

import com.omeracar.library.dto.request.LibraryRequestDto;
import com.omeracar.library.dto.response.LibraryResponseDto;
import com.omeracar.library.entity.Library;
import com.omeracar.library.exception.ResourceNotFoundException;
import com.omeracar.library.mapping.LibraryMapper;
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

import java.lang.module.ResolutionException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {

    private final ILibraryRepository libraryRepository;

    //create
    public ResponseEntity<LibraryResponseDto> createLibrary(LibraryRequestDto requestDto) {
        log.info("Creating new library with name {}", requestDto.getName());

        Library library = Library.builder()
                .name(requestDto.getName())
                .location(requestDto.getLocation())
                .phoneNumber(requestDto.getPhoneNumber())
                .build();

        Library savedLibrary = libraryRepository.save(library);

        log.info("Library created with ID: {}", savedLibrary.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LibraryMapper.toDto(savedLibrary));
    }

    //getAll pageable
    public ResponseEntity<Page<LibraryResponseDto>> getAllLibraries(
            String name, int page, int size) {
        log.info("Fetching all active libraries");

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Library> libraryPage = libraryRepository.findByRecIsDeletedFalse(pageable);

        if (libraryPage == null || libraryPage.isEmpty()) {
            throw new ResourceNotFoundException("Active library not found");
        }

        Page<LibraryResponseDto> dtoPage = libraryPage.map(LibraryMapper::toDto);

        return ResponseEntity.ok(dtoPage);
    }

    //getByName pageable
    public ResponseEntity<Page<LibraryResponseDto>> getLibrariesByName(
            String name, int page, int size) {

        //Library name e göre ascending sıralama
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Page<Library> libraries;

        if (name == null || name.isEmpty()) {
            libraries = libraryRepository.findByRecIsDeletedFalse(pageable);
        } else {
            libraries = libraryRepository.findByNameContainingAndRecIsDeletedFalse(name, pageable);
        }


        Page<LibraryResponseDto> dtoPage = libraries.map(LibraryMapper::toDto);

        return ResponseEntity.ok(dtoPage);
    }


    //getById
    public ResponseEntity<LibraryResponseDto> getLibraryById(Long id) {
        log.info("Fetching library with ID: {}", id);

        Library library = libraryRepository.findById(id)
                .filter(lib -> !lib.isRecIsDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found with id: " + id));


        return ResponseEntity.ok(LibraryMapper.toDto(library));
    }

    //update
    public ResponseEntity<LibraryResponseDto> updateLibrary(Long id, LibraryRequestDto requestDto) {
        log.info("Updating library with ID: {}", id);

        Library library = libraryRepository.findByIdAndRecIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library not found with id: " + id));

        if (library.isRecIsDeleted()) {
            throw new ResourceNotFoundException("Library not found with id: " + id);
        }

        library.setName(requestDto.getName());
        library.setLocation(requestDto.getLocation());
        library.setPhoneNumber(requestDto.getPhoneNumber());

        Library updatedLibrary = libraryRepository.save(library);

        return ResponseEntity.ok(LibraryMapper.toDto(updatedLibrary));
    }

    //soft delete
    public ResponseEntity<String> deleteLibrary(Long id) {
        log.info("Deleting library with ID: {}", id);

        Library library = libraryRepository.findByIdAndRecIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library not found with id: " + id));

        library.setRecIsDeleted(true);

        //soft delete yaptığımda buna bağlı tüm kitapların da soft delete olması lazım
        if (library.getBooks() != null){
            library.getBooks().forEach(book -> {
                book.setRecIsDeleted(true);
                log.info("Soft-delete book with ID: {}", book.getId());
            });
        }

        log.info("Library with ID: {} soft-deleted successfully", id);

        libraryRepository.save(library);

        return ResponseEntity.ok("Library deleted successfully");
    }

    //get activated
    public ResponseEntity<LibraryResponseDto> activateLibrary(Long id){
        log.info("Activating library with ID: {}", id);

        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library not found"));

        if (!library.isRecIsDeleted()) {
            log.warn("Library with ID {} is already active", id);

            return ResponseEntity.badRequest().body(null); //zaten aktifse
        }

        library.setRecIsDeleted(false);

        Library activatedLibrary = libraryRepository.save(library);
        log.info("library with ID: {} activated successfully", id);

        return ResponseEntity.ok(LibraryMapper.toDto(activatedLibrary));
    }

}
