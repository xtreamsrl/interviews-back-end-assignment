package it.ms.backendassignment.service;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.dto.CommentDto;
import it.ms.backendassignment.dto.DeleteDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Comment;
import it.ms.backendassignment.model.Post;
import it.ms.backendassignment.repository.CommentRepository;
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
    public CommentDto createComment(CommentDto commentIn) throws BAException {

        if (Objects.isNull(commentIn.getPostId()) || StringUtils.isBlank(commentIn.getText())) {
            throw new BAException(Constants.BAD_COMMENT, HttpStatus.BAD_REQUEST);
        }

        Post postById = postService.getPostById(commentIn.getPostId());

        Comment comment = new Comment();

        BeanUtils.copyProperties(commentIn, comment);
        comment.setCreationDate(LocalDateTime.now());
        comment.setUpdateDate(LocalDateTime.now());

        postById.addComment(comment);
        postById.setUpdateDate(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        CommentDto out = new CommentDto(savedComment);

        log.info("Added comment {} to post {}", out, postById);
        return out;
    }

    @Override
    public List<Comment> getCommentsFromPost(Integer pageNo, Integer pageSize, Long postId) {
        Pageable pagination = PageRequest.of(pageNo, pageSize, Sort.by("updateDate").descending());

        Page<Comment> pagedResult = commentRepository.findByPostId(pagination, postId);

        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    @Override
    public Comment getCommentById(Long commentId) throws BAException {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BAException(Constants.COMMENT_NOT_FOUND + " id: " + commentId, HttpStatus.NOT_FOUND));
    }

    @Override
    public DeleteDto deleteComment(Long commentId) {
        DeleteDto deleteDto = new DeleteDto();

        if (commentRepository.findById(commentId).isPresent()) {
            commentRepository.deleteById(commentId);

            deleteDto.setMessage("Deleted comment with id: " + commentId);
            deleteDto.setSuccess(Boolean.TRUE);
        } else {
            deleteDto.setMessage("Could not delete comment with id: " + commentId);
            deleteDto.setSuccess(Boolean.FALSE);
        }

        return deleteDto;
    }

    @Override
    public Comment editComment(Long commentId, CommentDto newComment) throws BAException {
        Comment oldComment = this.getCommentById(commentId);

        if (StringUtils.isNotBlank(newComment.getText())) {
            oldComment.setUpdateDate(LocalDateTime.now());
            oldComment.setText(newComment.getText());

            commentRepository.save(oldComment);
        }

        return oldComment;
    }
}
