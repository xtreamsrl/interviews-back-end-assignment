package it.ms.backendassignment.service;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.dto.DeleteDto;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.dto.PostDtoIn;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Post;
import it.ms.backendassignment.model.User;
import it.ms.backendassignment.repository.PostRepository;
import it.ms.backendassignment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public PostDto createPost(PostDtoIn postDtoIn) throws BAException {


        if (StringUtils.isBlank(postDtoIn.getTitle()) || StringUtils.isBlank(postDtoIn.getBody())) {
            throw new BAException(Constants.BAD_TITLE_OR_BODY, HttpStatus.BAD_REQUEST);
        } else if (userRepository.findByUsername(postDtoIn.getUsername()).isEmpty()) {
            throw new  BAException(Constants.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        Post postTmp = new Post();

        postTmp.setTitle(postDtoIn.getTitle());
        postTmp.setBody(postDtoIn.getBody());
        postTmp.setCreationDate(LocalDateTime.now());
        postTmp.setUpdateDate(LocalDateTime.now());

        Post savedPost = postRepository.save(postTmp);

        User user = userRepository.findByUsername(postDtoIn.getUsername()).get();
        user.addPost(savedPost);

        PostDto out = new PostDto(savedPost);

        log.info("Saved post: {}", savedPost);
        return out;
    }

    @Override
    public PostDto getPostDtoById(Long postId) throws BAException {
        return postRepository.findById(postId).map(PostDto::new).orElseThrow(() -> new BAException(Constants.POST_NOT_FOUND + " id: " + postId, HttpStatus.NOT_FOUND));
    }

    @Override
    public Post getPostById(Long postId) throws BAException {
        return postRepository.findById(postId).orElseThrow(() -> new BAException(Constants.POST_NOT_FOUND + " id: " + postId, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public List<PostDto> getPosts(Integer pageNo, Integer pageSize) {
        Pageable pagination = PageRequest.of(pageNo, pageSize, Sort.by("updateDate").descending());

        Page<Post> pagedResult = postRepository.findAll(pagination);

        List<PostDto> out = new ArrayList<>();

        if (pagedResult.hasContent()) {
            out = pagedResult.getContent()
                    .stream().map(PostDto::new)
                    .collect(Collectors.toList());
        }

        return out;
    }

    @Override
    @Transactional
    public DeleteDto deletePost(Long postId) {
        DeleteDto deleteDto = new DeleteDto();

        if (postRepository.findById(postId).isPresent()) {
            Post post = postRepository.findById(postId).get();
            User user = userRepository.findByUsername(post.getUser().getUsername()).get();

            user.removePost(post);
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
    public PostDto editPost(Long postId, PostDtoIn newPost) throws BAException {
        Post oldPost = postRepository.findById(postId).orElseThrow(() -> new BAException(Constants.POST_NOT_FOUND + " id: " + postId, HttpStatus.NOT_FOUND));
        boolean edited = StringUtils.isNotBlank(newPost.getTitle()) || StringUtils.isNotBlank(newPost.getBody());

        Optional.of(newPost).map(PostDtoIn::getTitle).ifPresent(oldPost::setTitle);
        Optional.of(newPost).map(PostDtoIn::getBody).ifPresent(oldPost::setBody);

        if (edited) {
            oldPost.setUpdateDate(LocalDateTime.now());
            postRepository.save(oldPost);
        }

        return new PostDto(oldPost);
    }
}
