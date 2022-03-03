package it.ms.backendassignment.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    Boolean success;
    private String username;
}
