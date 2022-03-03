package it.ms.backendassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDtoIn {
    private String title;
    private String body;
    private String username;
}
