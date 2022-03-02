package it.ms.backendassignment.service;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.dto.DeleteDto;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Post;
import it.ms.backendassignment.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Transactional
    @Override
    public Post createPost(PostDto postDtoIn) throws BAException {

        if (StringUtils.isBlank(postDtoIn.getTitle()) || StringUtils.isBlank(postDtoIn.getBody())) {
            throw new BAException(Constants.BAD_TITLE_OR_BODY, HttpStatus.BAD_REQUEST);
        }

        Post postTmp = new Post();

        BeanUtils.copyProperties(postDtoIn, postTmp);
        postTmp.setCreationDate(LocalDateTime.now());
        postTmp.setUpdateDate(LocalDateTime.now());

        Post out = postRepository.save(postTmp);
        log.info("Saved post: {}", out);
        return out;
    }

    @Override
    public Post getPostById(Long postId) throws BAException {
        return postRepository.findById(postId).orElseThrow(() -> new BAException(Constants.POST_NOT_FOUND + " id: " + postId, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Post> getPosts(Integer pageNo, Integer pageSize) {
        Pageable pagination = PageRequest.of(pageNo, pageSize, Sort.by("updateDate").descending());

        Page<Post> pagedResult = postRepository.findAll(pagination);

        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    @Override
    @Transactional
    public DeleteDto deletePost(Long postId) {
        DeleteDto deleteDto = new DeleteDto();

        if (postRepository.findById(postId).isPresent()) {
            postRepository.deleteById(postId);

            deleteDto.setMessage("Deleted post with id: " + postId);
            deleteDto.setSuccess(Boolean.TRUE);
        } else {
            deleteDto.setMessage("Could not delete post with id: " + postId);
            deleteDto.setSuccess(Boolean.FALSE);
        }

        return deleteDto;
    }

    @Override
    @Transactional
    public Post editPost(Long postId, PostDto newPost) throws BAException {
        Post oldPost = this.getPostById(postId);
        boolean edited = StringUtils.isNotBlank(newPost.getTitle()) || StringUtils.isNotBlank(newPost.getBody());

        Optional.of(newPost).map(PostDto::getTitle).ifPresent(oldPost::setTitle);
        Optional.of(newPost).map(PostDto::getBody).ifPresent(oldPost::setBody);

        if (edited) {
            oldPost.setUpdateDate(LocalDateTime.now());
            postRepository.save(oldPost);
        }

        return oldPost;
    }
}
