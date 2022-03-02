package it.ms.backendassignment.service;

import it.ms.backendassignment.dto.CommentDto;
import it.ms.backendassignment.dto.DeleteDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommentService {
    Comment createComment(CommentDto commentIn) throws BAException;

    List<Comment> getCommentsFromPost(Integer pageNo, Integer pageSize, Long postId);

    Comment getCommentById(Long commentId) throws BAException;

    DeleteDto deleteComment(Long commentId);

    Comment editComment(Long commentId, CommentDto newComment) throws BAException;
}
