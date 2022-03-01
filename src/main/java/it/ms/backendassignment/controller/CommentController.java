package it.ms.backendassignment.controller;

import it.ms.backendassignment.dto.CommentDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Comment;
import it.ms.backendassignment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> createComment(@RequestBody CommentDto commentIn) throws BAException {
        Comment comment = commentService.createComment(commentIn);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Comment>> getCommentsFromPost(@RequestParam Long postId) throws BAException {
        List<Comment> comments = commentService.getCommentsFromPost(postId);
        return ResponseEntity.ok(comments);
    }
}
