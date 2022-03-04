package it.ms.backendassignment.service;

import it.ms.backendassignment.dto.LoginResponseDto;
import it.ms.backendassignment.dto.UserDto;
import it.ms.backendassignment.dto.UserSignUpDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
    User createUser(UserSignUpDto userIn) throws BAException;

    LoginResponseDto loginUser(UserDto userIn) throws BAException;

    User findUserByName(String username) throws BAException;
}
