package it.ms.backendassignment.crud;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.domain.Comment;
import it.ms.backendassignment.domain.User;
import it.ms.backendassignment.dto.*;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.repository.CommentRepository;
import it.ms.backendassignment.repository.PostRepository;
import it.ms.backendassignment.repository.UserRepository;
import it.ms.backendassignment.service.CommentService;
import it.ms.backendassignment.service.PostService;
import it.ms.backendassignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
public class CommentTests {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void nukeDb() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

     UserDto createUser() throws BAException {
        UserSignUpDto userInput = new UserSignUpDto();

        userInput.setUsername("Carlo");
        userInput.setPassword("1234");
        userInput.setRepeatPassword("1234");

        User user = userService.createUser(userInput);
        UserDto out = new UserDto();
        BeanUtils.copyProperties(user, out);

        return out;
    }
    @Test
    void shouldAddCommentToPost() throws BAException, InterruptedException {
        UserDto user = createUser();
        PostDtoIn postIn = new PostDtoIn("A pretty title", "A pretty body", user.getUsername());

        PostDto out = postService.createPost(postIn);

        //Wait a bit so the update date is different from the creation one
        TimeUnit.SECONDS.sleep(1);

        CommentDtoIn commentIn = new CommentDtoIn();
        commentIn.setPostId(out.getId());
        commentIn.setText("This is a comment");
        commentIn.setUsername(user.getUsername());

        commentService.createComment(commentIn);

        PostDto postAfterComment = postService.getPostDtoById(out.getId());

        assertThat(postAfterComment.getComments()).hasSize(1);
        assertThat(postAfterComment.getUpdateDate().isAfter(postAfterComment.getCreationDate())).isTrue();
    }

    @Test
    void shouldGetCommentsFromPost() throws BAException {
        UserDto user = createUser();
        PostDtoIn postIn = new PostDtoIn("A pretty title", "A pretty body", user.getUsername());

        PostDto out = postService.createPost(postIn);

        CommentDtoIn commentIn = new CommentDtoIn();
        commentIn.setPostId(out.getId());
        commentIn.setText("This is a comment");
        commentIn.setUsername(user.getUsername());

        CommentDtoIn commentInTwo = new CommentDtoIn();
        commentInTwo.setPostId(out.getId());
        commentInTwo.setText("This is another comment");
        commentInTwo.setUsername(user.getUsername());

        commentService.createComment(commentIn);
        commentService.createComment(commentInTwo);

        PostDto postAfterComment = postService.getPostDtoById(out.getId());

        assertThat(postAfterComment.getComments()).hasSize(2);

        List<CommentDto> firstCommentPage = commentService.getCommentsFromPost(0, 1, out.getId());

        assertThat(firstCommentPage).hasSize(1);
    }

    @Test
    void shouldGetCommentById() throws BAException {
        UserDto user = createUser();
        PostDtoIn postIn = new PostDtoIn("A pretty title", "A pretty body", user.getUsername());

        PostDto out = postService.createPost(postIn);

        CommentDtoIn commentIn = new CommentDtoIn();
        commentIn.setPostId(out.getId());
        commentIn.setUsername(user.getUsername());
        commentIn.setText("This is a comment");

        CommentDto comment = commentService.createComment(commentIn);

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
    @Transactional
    void shouldDeleteComment() throws BAException {
        UserDto user = createUser();
        PostDtoIn postIn = new PostDtoIn("A pretty title", "A pretty body", user.getUsername());

        PostDto savedPost = postService.createPost(postIn);

        User author = userService.findUserByName(user.getUsername());

        CommentDtoIn commentIn = new CommentDtoIn();
        commentIn.setPostId(savedPost.getId());
        commentIn.setText("This is a comment");
        commentIn.setUsername(user.getUsername());

        CommentDto comment = commentService.createComment(commentIn);

        DeleteDto deleteDto = commentService.deleteComment(comment.getId());

        assertThat(deleteDto.getSuccess()).isTrue();
        assertThat(savedPost.getComments()).isEmpty();
        assertThat(author.getComments()).isEmpty();
    }

    @Test
    void shouldntDeletePost() throws BAException {
        DeleteDto deleteDto = commentService.deleteComment(2L);
        assertThat(deleteDto.getSuccess()).isFalse();
    }

    @Test
    void shouldUpdateComment() throws BAException, InterruptedException {
        UserDto user = createUser();
        PostDtoIn postIn = new PostDtoIn("A pretty title", "A pretty body", user.getUsername());

        PostDto out = postService.createPost(postIn);

        //Wait a bit so the update date is different from the creation one
        TimeUnit.SECONDS.sleep(1);

        CommentDtoIn commentIn = new CommentDtoIn();
        commentIn.setPostId(out.getId());
        commentIn.setText("This is a comment");
        commentIn.setUsername(user.getUsername());

        CommentDto comment = commentService.createComment(commentIn);

        CommentDtoIn newComment = new CommentDtoIn();
        newComment.setPostId(comment.getId());
        newComment.setText("Edited text");


        CommentDto editedComment = commentService.editComment(comment.getId(), newComment);

        assertThat(editedComment.getText()).isEqualTo(newComment.getText());
        assertThat(editedComment.getUpdateDate().isAfter(out.getUpdateDate())).isTrue();
    }

    @Test
    void shouldntUpdateComment() throws BAException {
        UserDto user = createUser();
        PostDtoIn postIn = new PostDtoIn("A pretty title", "A pretty body", user.getUsername());
        PostDto out = postService.createPost(postIn);

        CommentDtoIn commentIn = new CommentDtoIn();
        commentIn.setPostId(out.getId());
        commentIn.setText("This is a comment");
        commentIn.setUsername(user.getUsername());

        CommentDto comment = commentService.createComment(commentIn);

        CommentDtoIn newComment = new CommentDtoIn();

        CommentDto editedComment = commentService.editComment(comment.getId(), newComment);
        assertThat(editedComment.getText()).isEqualTo(comment.getText());
    }
}
