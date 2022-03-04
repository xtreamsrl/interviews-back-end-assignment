package it.ms.backendassignment.controller;

import it.ms.backendassignment.domain.User;
import it.ms.backendassignment.dto.LoginResponseDto;
import it.ms.backendassignment.dto.UserDto;
import it.ms.backendassignment.dto.UserSignUpDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody UserSignUpDto userSignUpDto) throws BAException {
        User user = userService.createUser(userSignUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, path = "/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserDto userDto) throws BAException {
        LoginResponseDto responseDto = userService.loginUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }



}
