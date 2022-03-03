package it.ms.backendassignment.service;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.dto.LoginResponseDto;
import it.ms.backendassignment.dto.UserDto;
import it.ms.backendassignment.dto.UserSignUpDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.User;
import it.ms.backendassignment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(UserSignUpDto userIn) throws BAException {
        if (userRepository.findByUsername(userIn.getUsername()).isPresent()) {
            throw new BAException(Constants.USERNAME_ALREADY_PRESENT, HttpStatus.BAD_REQUEST);
        } else if (StringUtils.isBlank(userIn.getUsername()) || StringUtils.isBlank(userIn.getPassword())) {
            throw new BAException(Constants.BAD_USER_LOGIN_INPUT, HttpStatus.BAD_REQUEST);
        } else if (!StringUtils.equals(userIn.getPassword(), userIn.getRepeatPassword())) {
            throw new BAException(Constants.PASSWORD_DO_NOT_MATCH, HttpStatus.BAD_REQUEST);
        }

        User user = new User();

        user.setUsername(userIn.getUsername());
        //very secure "encryption" :)
        user.setPassword(Base64.getEncoder().encodeToString(userIn.getPassword().getBytes()));
        user.setCreationDate(LocalDateTime.now());

        User out = userRepository.save(user);

        log.info("Saved user {}", out);
        return out;
    }

    @Override
    public LoginResponseDto loginUser(UserDto userIn) throws BAException {
        if (StringUtils.isBlank(userIn.getUsername()) || StringUtils.isBlank(userIn.getPassword())) {
            throw new BAException(Constants.BAD_USER_LOGIN_INPUT, HttpStatus.BAD_REQUEST);
        } else if (userRepository.findByUsername(userIn.getUsername()).isEmpty()) {
            throw new BAException(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        User user = userRepository.findByUsername(userIn.getUsername()).get();

        if (Base64.getEncoder().encodeToString(userIn.getPassword().getBytes()).equals(user.getPassword())) {
            LoginResponseDto responseDto = new LoginResponseDto();
            responseDto.setSuccess(Boolean.TRUE);
            responseDto.setUsername(userIn.getUsername());

            log.info("Authenticated user {}", userIn.getUsername());

            return responseDto;
        } else {
            throw new BAException(Constants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
    }
}
