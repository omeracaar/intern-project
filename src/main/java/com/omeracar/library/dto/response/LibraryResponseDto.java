package com.omeracar.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryResponseDto {

    private Long id;

    private String name;

    private String location;

    private String phoneNumber;

    private LocalDateTime updatedAt;

    private List<String> bookDetails;
}