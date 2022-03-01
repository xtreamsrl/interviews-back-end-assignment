package it.ms.backendassignment.controller;

import it.ms.backendassignment.dto.DeletePostDto;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Post;
import it.ms.backendassignment.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> createPost(@RequestBody PostDto postIn) throws BAException {
        Post post = postService.createPost(postIn);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) throws BAException {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok().body(post);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Post>> getPosts(@RequestParam(defaultValue = "0") Integer pageNo,
                                               @RequestParam(defaultValue = "5") Integer pageSize) {
        List<Post> posts = postService.getPosts(pageNo, pageSize);
        return ResponseEntity.ok(posts);
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public ResponseEntity<DeletePostDto> deletePostById(@PathVariable Long id) {
        DeletePostDto deletePostDto = postService.deletePost(id);
        return ResponseEntity.ok(deletePostDto);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public ResponseEntity<Post> editPostById(@PathVariable Long id, @RequestBody PostDto newPost) throws BAException {
        Post post = postService.editPost(id, newPost);
        return ResponseEntity.ok(post);
    }


}
