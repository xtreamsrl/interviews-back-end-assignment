package it.ms.backendassignment.controller;

import it.ms.backendassignment.domain.User;
import it.ms.backendassignment.dto.*;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.service.SearchService;
import it.ms.backendassignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SearchService searchService;

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

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/searchPosts")
    public ResponseEntity<List<PostDto>> searchPostsByUsername(@RequestParam(defaultValue = "0") Integer pageNo,
                                                               @RequestParam(defaultValue = "5") Integer pageSize,
                                                               @RequestParam String q) {
        List<PostDto> posts = searchService.searchPostsByUser(pageNo, pageSize, q);
        return ResponseEntity.ok(posts);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/searchComments")
    public ResponseEntity<List<CommentDto>> searchCommentsByUsername(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                     @RequestParam(defaultValue = "5") Integer pageSize,
                                                                     @RequestParam String q) {
        List<CommentDto> comments = searchService.searchCommentsByUser(pageNo, pageSize, q);
        return ResponseEntity.ok(comments);
    }


}
