package it.ms.backendassignment.service;

import it.ms.backendassignment.dto.CommentDto;
import it.ms.backendassignment.dto.CommentDtoIn;
import it.ms.backendassignment.dto.DeleteDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommentService {
    CommentDto createComment(CommentDtoIn commentIn) throws BAException;

    List<CommentDto> getCommentsFromPost(Integer pageNo, Integer pageSize, Long postId);

    Comment getCommentById(Long commentId) throws BAException;

    CommentDto getCommentDtoById(Long commentId) throws BAException;

    DeleteDto deleteComment(Long commentId);

    CommentDto editComment(Long commentId, CommentDtoIn newComment) throws BAException;
}
