package com.omeracar.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryResponseDto {

    private Long id;

    private String name;

    private String location;

    private String phoneNumber;

    private List<String> bookDetails;

}