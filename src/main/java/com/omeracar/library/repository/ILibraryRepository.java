package com.omeracar.library.repository;

import com.omeracar.library.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILibraryRepository extends JpaRepository<Library,Long> {
}
