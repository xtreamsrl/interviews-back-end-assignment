package it.ms.backendassignment.service;

import it.ms.backendassignment.dto.DeletePostDto;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PostService {

    Post createPost(PostDto postDtoIn) throws BAException;

    Post getPostById(Long postId) throws BAException;

    List<Post> getPosts(Integer pageNo, Integer pageSize);

    DeletePostDto deletePost(Long postId);

    Post editPost(Long postId, PostDto newPost) throws BAException;
}
