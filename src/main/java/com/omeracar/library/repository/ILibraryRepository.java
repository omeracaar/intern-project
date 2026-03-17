package com.omeracar.library.repository;

import com.omeracar.library.entity.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ILibraryRepository extends JpaRepository<Library,Long> {

    List<Library> findRecIsDeletedFalse();

    Optional<Library> findByIdAndRecIsDeletedFalse(Long id);

    Page<Library> findByRecIsDeletedFalse(Pageable pageable);

    Page<Library> findByNameContainingAndRecIsDeletedFalse(String name, Pageable pageable);
}
