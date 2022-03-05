package it.ms.backendassignment.service;

import it.ms.backendassignment.domain.Post;
import it.ms.backendassignment.dto.DeleteDto;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.dto.PostDtoIn;
import it.ms.backendassignment.exception.BAException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PostService {
    PostDto createPost(PostDtoIn postDtoIn) throws BAException;

    PostDto getPostDtoById(Long postId) throws BAException;

    Post getPostById(Long postId) throws BAException;

    List<PostDto> getPosts(Integer pageNo, Integer pageSize);

    DeleteDto deletePost(Long postId) throws BAException;

    PostDto editPost(Long postId, PostDtoIn newPost) throws BAException;
}
