package it.ms.backendassignment.service;

import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Post;
import org.springframework.stereotype.Component;

@Component
public interface PostService {

    Post createPost(PostDto postDtoIn) throws BAException;
}
