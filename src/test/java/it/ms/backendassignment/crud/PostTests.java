package it.ms.backendassignment.crud;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.domain.Post;
import it.ms.backendassignment.domain.User;
import it.ms.backendassignment.dto.*;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.repository.PostRepository;
import it.ms.backendassignment.repository.UserRepository;
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

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class PostTests {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void nukeDb() {
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
    void shouldNotPersistBadPosts() {
        PostDtoIn postWithNoTitle = new PostDtoIn();
        postWithNoTitle.setBody("This should not be saved");

        PostDtoIn postWithNoBody = new PostDtoIn();
        postWithNoBody.setTitle("This should not be saved either");

        assertThatThrownBy(() -> postService.createPost(postWithNoTitle))
                .isInstanceOf(BAException.class)
                .hasMessage(Constants.BAD_TITLE_OR_BODY);

        assertThatThrownBy(() -> postService.createPost(postWithNoBody))
                .isInstanceOf(BAException.class)
                .hasMessage(Constants.BAD_TITLE_OR_BODY);
    }

    @Test
    void shouldPersistPosts() {
        try {
            UserDto user = createUser();
            PostDtoIn postIn = new PostDtoIn("A pretty title","A pretty body", user.getUsername());

            PostDto out = postService.createPost(postIn);

            assertThat(out.getId()).isNotNull();
            assertThat(out.getTitle()).isEqualTo(postIn.getTitle());
            assertThat(out.getBody()).isEqualTo(postIn.getBody());
        } catch (BAException e) {
            fail("Shouldn't have thrown BAException");
        }
    }

    @Test
    void shouldGetPostById() throws BAException {
        UserDto user = createUser();
        PostDtoIn postIn = new PostDtoIn("A pretty title", "A pretty body", user.getUsername());

        PostDto out = postService.createPost(postIn);

        Post postById = postService.getPostById(out.getId());

        assertThat(postById.getId()).isEqualTo(out.getId());
    }

    @Test
    void shouldntGetPostById() {
        assertThatThrownBy(() -> postService.getPostById(23L))
                .isInstanceOf(BAException.class)
                .hasMessageStartingWith(Constants.POST_NOT_FOUND);
    }

    @Test
    void shouldGetPostsByPage() throws BAException, InterruptedException {

        UserDto user = createUser();

        List<PostDtoIn> postList = List.of(new PostDtoIn("Title 1", "Body 1", user.getUsername()),
                new PostDtoIn("Title 2", "Body 2", user.getUsername()),
                new PostDtoIn("Title 3", "Body 3", user.getUsername()),
                new PostDtoIn("Title 4", "Body 4", user.getUsername()),
                new PostDtoIn("Title 5", "Body 5", user.getUsername()),
                new PostDtoIn("Title 6", "Body 6", user.getUsername()),
                new PostDtoIn("Title 7", "Body 7", user.getUsername()));

        for (PostDtoIn post : postList) {
            postService.createPost(post);
            //sleep for 1 second to have different creation dates
            TimeUnit.SECONDS.sleep(1);
        }

        List<PostDto> firstPage = postService.getPosts(0, 5);
        List<PostDto> secondPage = postService.getPosts(1, 5);

        assertThat(firstPage).hasSize(5);
        assertThat(secondPage).hasSize(2);

        //checks that the first item is the most recent one
        PostDto mostRecentPost = firstPage.get(0);

        for (int i = 1, firstPageSize = firstPage.size(); i < firstPageSize; i++) {
            PostDto post = firstPage.get(i);
            assertThat(mostRecentPost
                    .getCreationDate()
                    .isAfter(post.getCreationDate())
            ).isTrue();
        }
    }

    @Test
    @Transactional
    void shouldDeletePost() throws BAException {
        UserDto user = createUser();
        PostDtoIn postIn = new PostDtoIn("A pretty title", "A pretty body", user.getUsername());
        PostDto out = postService.createPost(postIn);

        DeleteDto deleteDto = postService.deletePost(out.getId());

        String username = out.getAuthorDetails().getUsername();
        User author = userService.findUserByName(username);

        assertThat(author.getPosts()).isEmpty();
        assertThat(deleteDto.getSuccess()).isTrue();
    }

    @Test
    void shouldntDeletePost() throws BAException {
        DeleteDto deleteDto = postService.deletePost(2L);
        assertThat(deleteDto.getSuccess()).isFalse();
    }

    @Test
    void shouldUpdatePost() throws BAException, InterruptedException {
        UserDto user = createUser();
        PostDtoIn postIn = new PostDtoIn("A pretty title", "A pretty body", user.getUsername());
        PostDto out = postService.createPost(postIn);

        //Wait a bit so the update date is different from the creation one
        TimeUnit.SECONDS.sleep(1);

        PostDtoIn newPost = new PostDtoIn();

        newPost.setTitle("An even prettier title");

        PostDto editedPost = postService.editPost(out.getId(), newPost);

        assertThat(editedPost.getTitle()).isEqualTo(editedPost.getTitle());
        assertThat(editedPost.getBody()).isEqualTo(out.getBody());
        assertThat(editedPost.getUpdateDate().isAfter(out.getUpdateDate())).isTrue();
    }

    @Test
    void shouldntUpdatePost() throws BAException {
        UserDto user = createUser();
        PostDtoIn postIn = new PostDtoIn("A pretty title", "A pretty body", user.getUsername());
        PostDto out = postService.createPost(postIn);


        PostDtoIn newPost = new PostDtoIn();

        PostDto editedPost = postService.editPost(out.getId(), newPost);
        assertThat(editedPost.getTitle()).isEqualTo(out.getTitle());
        assertThat(editedPost.getBody()).isEqualTo(out.getBody());
    }
}
