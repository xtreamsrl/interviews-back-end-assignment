package it.ms.backendassignment.service;

import it.ms.backendassignment.dto.CommentDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommentService {
    Comment createComment(CommentDto commentIn) throws BAException;

    List<Comment> getCommentsFromPost(Long postId) throws BAException;
}
