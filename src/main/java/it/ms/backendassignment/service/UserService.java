package it.ms.backendassignment.service;

import it.ms.backendassignment.dto.UserDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
    User createUser(UserDto userIn) throws BAException;

    User loginUser(UserDto userIn) throws BAException;
}
