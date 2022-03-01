package it.ms.backendassignment.service;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.dto.CommentDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Comment;
import it.ms.backendassignment.model.Post;
import it.ms.backendassignment.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment createComment(CommentDto commentIn) throws BAException {

        if (Objects.isNull(commentIn.getPostId()) || StringUtils.isBlank(commentIn.getText())) {
            throw new BAException(Constants.BAD_COMMENT, HttpStatus.BAD_REQUEST);
        }

        Post postById = postService.getPostById(commentIn.getPostId());

        Comment comment = new Comment();

        comment.setPost(postById);
        comment.setText(commentIn.getText());
        comment.setCreationDate(LocalDateTime.now());
        comment.setUpdateDate(LocalDateTime.now());

        postById.addComment(comment);
        postById.setUpdateDate(LocalDateTime.now());

        Comment out = commentRepository.save(comment);

        log.info("Added comment {} to post {}", out, postById);
        return out;
    }

    @Override
    public List<Comment> getCommentsFromPost(Long postId) throws BAException {
        Post postById = postService.getPostById(postId);
        return new ArrayList<>(postById.getComments());
    }
}
