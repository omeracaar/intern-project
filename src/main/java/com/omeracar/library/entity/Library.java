package com.omeracar.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "libraries")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Library extends BaseEntity{

    private String name;

    private String location;

    private String phoneNumber;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;
}
