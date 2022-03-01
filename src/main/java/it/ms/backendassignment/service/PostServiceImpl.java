package it.ms.backendassignment.service;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Post;
import it.ms.backendassignment.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Transactional
    @Override
    public Post createPost(PostDto postDtoIn) throws BAException {

        if (StringUtils.isBlank(postDtoIn.getTitle()) || StringUtils.isBlank(postDtoIn.getBody())){
            throw new BAException(Constants.BAD_TITLE_OR_BODY, HttpStatus.BAD_REQUEST);
        }
        try {
            Post postTmp = new Post();
            BeanUtils.copyProperties(postDtoIn, postTmp);
            postTmp.setCreationDate(LocalDateTime.now());
            postTmp.setUpdateDate(LocalDateTime.now());

            Post out = postRepository.save(postTmp);
            log.info("Saved post: {}", out);
            return out;
        } catch (Exception e) {
            throw new BAException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
