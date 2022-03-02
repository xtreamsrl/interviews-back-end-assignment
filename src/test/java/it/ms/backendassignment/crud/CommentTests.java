package it.ms.backendassignment.crud;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.dto.CommentDto;
import it.ms.backendassignment.dto.DeleteDto;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Comment;
import it.ms.backendassignment.model.Post;
import it.ms.backendassignment.repository.CommentRepository;
import it.ms.backendassignment.service.CommentService;
import it.ms.backendassignment.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
public class CommentTests {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;


    @BeforeEach
    void nukeDb() {
        commentRepository.deleteAll();
    }

    @Test
    @Transactional
    void shouldAddCommentToPost() throws BAException {
        PostDto postIn = new PostDto();
        postIn.setTitle("A pretty title");
        postIn.setBody("A pretty body");

        Post out = postService.createPost(postIn);

        CommentDto commentIn = new CommentDto();
        commentIn.setPostId(out.getId());
        commentIn.setText("This is a comment");

        commentService.createComment(commentIn);

        Post postAfterComment = postService.getPostById(out.getId());

        assertThat(postAfterComment.getComments()).hasSize(1);
        assertThat(postAfterComment.getUpdateDate().isAfter(postAfterComment.getCreationDate())).isTrue();
    }

    @Test
    @Transactional
    void shouldGetCommentsFromPost() throws BAException {
        PostDto postIn = new PostDto();
        postIn.setTitle("A pretty title");
        postIn.setBody("A pretty body");

        Post out = postService.createPost(postIn);

        CommentDto commentIn = new CommentDto();
        commentIn.setPostId(out.getId());
        commentIn.setText("This is a comment");

        CommentDto commentInTwo = new CommentDto();
        commentInTwo.setPostId(out.getId());
        commentInTwo.setText("This is another comment");

        commentService.createComment(commentIn);
        commentService.createComment(commentInTwo);

        Post postAfterComment = postService.getPostById(out.getId());

        assertThat(postAfterComment.getComments()).hasSize(2);

        List<Comment> firstCommentPage = commentService.getCommentsFromPost(0, 1, out.getId());

        assertThat(firstCommentPage).hasSize(1);
    }

    @Test
    void shouldGetCommentById() throws BAException {
        PostDto postIn = new PostDto();
        postIn.setTitle("A pretty title");
        postIn.setBody("A pretty body");
        Post out = postService.createPost(postIn);

        CommentDto commentIn = new CommentDto();
        commentIn.setPostId(out.getId());
        commentIn.setText("This is a comment");

        Comment comment = commentService.createComment(commentIn);

        Comment commentById = commentService.getCommentById(comment.getId());

        assertThat(commentById.getId()).isEqualTo(comment.getId());
    }

    @Test
    void shouldntGetCommentById() {
        assertThatThrownBy(() -> commentService.getCommentById(23L))
                .isInstanceOf(BAException.class)
                .hasMessageStartingWith(Constants.COMMENT_NOT_FOUND);
    }

    @Test
    void shouldDeleteComment() throws BAException {
        PostDto postIn = new PostDto();
        postIn.setTitle("A pretty title");
        postIn.setBody("A pretty body");
        Post out = postService.createPost(postIn);

        CommentDto commentIn = new CommentDto();
        commentIn.setPostId(out.getId());
        commentIn.setText("This is a comment");

        Comment comment = commentService.createComment(commentIn);

        DeleteDto deleteDto = commentService.deleteComment(comment.getId());

        assertThat(deleteDto.getSuccess()).isTrue();
    }

    @Test
    void shouldntDeletePost() {
        DeleteDto deleteDto = commentService.deleteComment(2L);
        assertThat(deleteDto.getSuccess()).isFalse();
    }

    @Test
    void shouldUpdateComment() throws BAException {
        PostDto postIn = new PostDto();
        postIn.setTitle("A pretty title");
        postIn.setBody("A pretty body");
        Post out = postService.createPost(postIn);

        CommentDto commentIn = new CommentDto();
        commentIn.setPostId(out.getId());
        commentIn.setText("This is a comment");

        Comment comment = commentService.createComment(commentIn);

        CommentDto newComment = new CommentDto();
        newComment.setPostId(comment.getId());
        newComment.setText("Edited text");

        Comment editedComment = commentService.editComment(comment.getId(), newComment);

        assertThat(editedComment.getText()).isEqualTo(newComment.getText());
        assertThat(editedComment.getUpdateDate().isAfter(out.getUpdateDate())).isTrue();
    }

    @Test
    void shouldntUpdateComment() throws BAException {
        PostDto postIn = new PostDto();
        postIn.setTitle("A pretty title");
        postIn.setBody("A pretty body");
        Post out = postService.createPost(postIn);

        CommentDto commentIn = new CommentDto();
        commentIn.setPostId(out.getId());
        commentIn.setText("This is a comment");

        Comment comment = commentService.createComment(commentIn);

        CommentDto newComment = new CommentDto();

        Comment editedComment = commentService.editComment(comment.getId(), newComment);
        assertThat(editedComment.getText()).isEqualTo(comment.getText());
    }
}
