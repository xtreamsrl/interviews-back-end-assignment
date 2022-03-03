package it.ms.backendassignment.dto;

import lombok.Data;

@Data
public class UserSignUpDto extends UserDto {
    private String repeatPassword;
}
