package it.ms.backendassignment.controller;

import it.ms.backendassignment.dto.CommentDto;
import it.ms.backendassignment.dto.DeleteDto;
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
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentIn) throws BAException {
        CommentDto comment = commentService.createComment(commentIn);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Comment>> getCommentsFromPost(@RequestParam(defaultValue = "0") Integer pageNo,
                                                             @RequestParam(defaultValue = "5") Integer pageSize,
                                                             @RequestParam Long postId) {
        List<Comment> comments = commentService.getCommentsFromPost(pageNo, pageSize, postId);
        return ResponseEntity.ok(comments);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) throws BAException {
        Comment comment = commentService.getCommentById(id);
        return ResponseEntity.ok().body(comment);
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public ResponseEntity<DeleteDto> deleteCommentById(@PathVariable Long id) {
        DeleteDto deleteDto = commentService.deleteComment(id);
        return ResponseEntity.ok(deleteDto);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public ResponseEntity<Comment> editCommentById(@PathVariable Long id, @RequestBody CommentDto newComment) throws BAException {
        Comment comment = commentService.editComment(id, newComment);
        return ResponseEntity.ok(comment);
    }
}
