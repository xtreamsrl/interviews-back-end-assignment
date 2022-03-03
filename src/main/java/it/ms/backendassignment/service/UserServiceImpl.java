package it.ms.backendassignment.service;

import it.ms.backendassignment.dto.UserDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User createUser(UserDto userIn) throws BAException {
        return null;
    }

    @Override
    public User loginUser(UserDto userIn) throws BAException {
        return null;
    }
}
