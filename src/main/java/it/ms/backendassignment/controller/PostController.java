package it.ms.backendassignment.controller;

import it.ms.backendassignment.dto.DeleteDto;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.dto.PostDtoIn;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.service.PostService;
import it.ms.backendassignment.service.SearchService;
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

    @Autowired
    private SearchService searchService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDto> createPost(@RequestBody PostDtoIn postIn) throws BAException {
        PostDto post = postService.createPost(postIn);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) throws BAException {
        PostDto post = postService.getPostDtoById(id);
        return ResponseEntity.ok().body(post);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostDto>> getPosts(@RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "5") Integer pageSize) {
        List<PostDto> posts = postService.getPosts(pageNo, pageSize);
        return ResponseEntity.ok(posts);
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public ResponseEntity<DeleteDto> deletePostById(@PathVariable Long id) throws BAException {
        DeleteDto deleteDto = postService.deletePost(id);
        return ResponseEntity.ok(deleteDto);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public ResponseEntity<PostDto> editPostById(@PathVariable Long id, @RequestBody PostDtoIn newPost) throws BAException {
        PostDto post = postService.editPost(id, newPost);
        return ResponseEntity.ok(post);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/search")
    public ResponseEntity<List<PostDto>> searchByKeyword(@RequestParam(defaultValue = "0") Integer pageNo,
                                                         @RequestParam(defaultValue = "5") Integer pageSize,
                                                         @RequestParam String q) {
        List<PostDto> posts = searchService.searchPostsByKeyword(pageNo, pageSize, q);
        return ResponseEntity.ok(posts);
    }


}
