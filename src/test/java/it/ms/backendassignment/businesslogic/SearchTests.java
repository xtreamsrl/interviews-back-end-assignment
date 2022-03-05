package it.ms.backendassignment.businesslogic;

import it.ms.backendassignment.domain.User;
import it.ms.backendassignment.dto.*;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.repository.CommentRepository;
import it.ms.backendassignment.repository.PostRepository;
import it.ms.backendassignment.repository.UserRepository;
import it.ms.backendassignment.service.CommentService;
import it.ms.backendassignment.service.PostService;
import it.ms.backendassignment.service.SearchService;
import it.ms.backendassignment.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SearchTests {

    @Autowired
    private SearchService searchService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

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
    void shouldFindPostsWithKeywordInTitle() throws BAException {
        UserDto user = createUser();

        List<PostDtoIn> postList = List.of(new PostDtoIn("Title 1", "Body 1", user.getUsername()),
                new PostDtoIn("Title 2", "Body 2", user.getUsername()),
                new PostDtoIn("Title 3", "Body 3", user.getUsername()),
                new PostDtoIn("Hello again", "World 4", user.getUsername()),
                new PostDtoIn("Title 5", "Body 5", user.getUsername()),
                new PostDtoIn("Title 6", "Body 6", user.getUsername()),
                new PostDtoIn("Hello 7", "Hi", user.getUsername()),
                new PostDtoIn("Hello", "World", user.getUsername()));

        for (PostDtoIn post : postList) {
            postService.createPost(post);
        }

        String keyword = "hello";
        List<PostDto> posts = searchService.searchPostsByKeyword(0, 5, keyword);

        assertThat(posts).hasSize(3);
        assertThat(posts.stream().filter(post -> StringUtils.containsIgnoreCase(post.getTitle(), keyword))).hasSize(3);
    }

    @Test
    void shouldFindPostsWithKeywordInBody() throws BAException {
        UserDto user = createUser();

        List<PostDtoIn> postList = List.of(new PostDtoIn("Title 1", "Body 1", user.getUsername()),
                new PostDtoIn("Title 2", "Body 2", user.getUsername()),
                new PostDtoIn("Title 3", "Body 3", user.getUsername()),
                new PostDtoIn("Hello again", "World 4", user.getUsername()),
                new PostDtoIn("Title 5", "Body 5", user.getUsername()),
                new PostDtoIn("Title 6", "Body 6", user.getUsername()),
                new PostDtoIn("Hello 7", "Hi", user.getUsername()),
                new PostDtoIn("Hello", "World", user.getUsername()));

        for (PostDtoIn post : postList) {
            postService.createPost(post);
        }

        String keyword = "hi";
        List<PostDto> posts = searchService.searchPostsByKeyword(0, 5, keyword);

        assertThat(posts).hasSize(1);
        assertThat(posts.stream().filter(post -> StringUtils.containsIgnoreCase(post.getBody(), keyword))).hasSize(1);
    }

    @Test
    void shouldFindPostsWithKeywordInTitleAndBody() throws BAException {
        UserDto user = createUser();
        List<PostDtoIn> postList = List.of(
                new PostDtoIn("Hello", "Body", user.getUsername()),
                new PostDtoIn("Word", "Hello", user.getUsername()),
                new PostDtoIn("Algorithms", "Data", user.getUsername()),
                new PostDtoIn("Another title", "Another body", user.getUsername())
        );

        for (PostDtoIn post : postList) {
            postService.createPost(post);
        }

        String keyword = "hello";
        List<PostDto> posts = searchService.searchPostsByKeyword(0, 5, keyword);

        assertThat(posts).hasSize(2);
        assertThat(posts.stream().filter(post -> StringUtils.containsIgnoreCase(post.getTitle(), keyword) ||
                StringUtils.containsIgnoreCase(post.getBody(), keyword))).hasSize(2);
    }

    @Test
    void shouldReturnEmptyListWhenNoMatches() throws BAException {
        UserDto user = createUser();

        List<PostDtoIn> postList = List.of(new PostDtoIn("Title 1", "Body 1", user.getUsername()),
                new PostDtoIn("Title 2", "Body 2", user.getUsername()),
                new PostDtoIn("Title 3", "Body 3", user.getUsername()),
                new PostDtoIn("Hello again", "World 4", user.getUsername()),
                new PostDtoIn("Title 5", "Body 5", user.getUsername()),
                new PostDtoIn("Title 6", "Body 6", user.getUsername()),
                new PostDtoIn("Hello 7", "Hi", user.getUsername()),
                new PostDtoIn("Hello", "World", user.getUsername()));

        for (PostDtoIn post : postList) {
            postService.createPost(post);
        }

        String keyword = "ciao";
        List<PostDto> posts = searchService.searchPostsByKeyword(0, 5, keyword);

        assertThat(posts).isEmpty();
    }

    @Test
    void shouldFindPostsByUsername() throws BAException {
        //create three users
        UserDto carlo = createUser();

        UserSignUpDto marioDto = new UserSignUpDto();

        marioDto.setUsername("Mario");
        marioDto.setPassword("1234");
        marioDto.setRepeatPassword("1234");

        User marioOut = userService.createUser(marioDto);
        UserDto mario = new UserDto();
        BeanUtils.copyProperties(marioOut, mario);

        UserSignUpDto francoDto = new UserSignUpDto();

        francoDto.setUsername("Franco");
        francoDto.setPassword("1234");
        francoDto.setRepeatPassword("1234");

        User francoOut = userService.createUser(francoDto);
        UserDto franco = new UserDto();
        BeanUtils.copyProperties(francoOut, franco);

        //make two of them post
        List<PostDtoIn> postList = List.of(new PostDtoIn("Carlo's post #1", "A pretty body", carlo.getUsername()),
                new PostDtoIn("Mario's post #1", "Another pretty body", mario.getUsername()),
                new PostDtoIn("Carlo's post #2", "I don't know", carlo.getUsername()),
                new PostDtoIn("Mario's post #2", "What to write in these", mario.getUsername())
        );

        for (PostDtoIn post : postList) {
            postService.createPost(post);
        }

        //search Mario's posts
        List<PostDto> mariosPosts = searchService.searchPostsByUser(0, 5, mario.getUsername());
        //search Carlo's posts
        List<PostDto> carlosPosts = searchService.searchPostsByUser(0, 5, carlo.getUsername());
        //search Franco's posts
        List<PostDto> francosPosts = searchService.searchPostsByUser(0, 5, franco.getUsername());

        //assertions
        assertThat(mariosPosts).hasSize(2);
        mariosPosts.forEach(post -> assertThat(post.getAuthorDetails().getUsername()).isEqualTo(mario.getUsername()));

        assertThat(carlosPosts).hasSize(2);
        carlosPosts.forEach(post -> assertThat(post.getAuthorDetails().getUsername()).isEqualTo(carlo.getUsername()));

        assertThat(francosPosts).isEmpty();
    }

    @Test
    void shouldFindCommentsByUsername() throws BAException {
        //create three users
        UserDto carlo = createUser();

        UserSignUpDto marioDto = new UserSignUpDto();

        marioDto.setUsername("Mario");
        marioDto.setPassword("1234");
        marioDto.setRepeatPassword("1234");

        User marioOut = userService.createUser(marioDto);
        UserDto mario = new UserDto();
        BeanUtils.copyProperties(marioOut, mario);

        UserSignUpDto francoDto = new UserSignUpDto();

        francoDto.setUsername("Franco");
        francoDto.setPassword("1234");
        francoDto.setRepeatPassword("1234");

        User francoOut = userService.createUser(francoDto);
        UserDto franco = new UserDto();
        BeanUtils.copyProperties(francoOut, franco);

        //make two of them post
        List<PostDtoIn> postList = List.of(new PostDtoIn("Carlo's post #1", "A pretty body", carlo.getUsername()),
                new PostDtoIn("Mario's post #1", "Another pretty body", mario.getUsername()),
                new PostDtoIn("Carlo's post #2", "I don't know", carlo.getUsername()),
                new PostDtoIn("Mario's post #2", "What to write in these", mario.getUsername())
        );

        List<PostDto> savedPosts = new ArrayList<>();

        for (PostDtoIn post : postList) {
            savedPosts.add(postService.createPost(post));
        }

        //add comments
        for (int i = 0; i < savedPosts.size(); i++) {
            PostDto post = savedPosts.get(i);

            CommentDtoIn commentIn = new CommentDtoIn();
            commentIn.setPostId(post.getId());

            String username;

            if ((i % 2) == 0) {
                username = mario.getUsername();
            } else {
                username = carlo.getUsername();
            }
            commentIn.setText("This is a comment from" + username);
            commentIn.setUsername(username);

            commentService.createComment(commentIn);
        }

        //search Carlo's comments
        List<CommentDto> carlosComments = searchService.searchCommentsByUser(0, 5, carlo.getUsername());
        //search Mario's comments
        List<CommentDto> mariosComments = searchService.searchCommentsByUser(0, 5, mario.getUsername());
        //search Franco's comments
        List<CommentDto> francoComments = searchService.searchCommentsByUser(0, 5, franco.getUsername());

        //assertions
        assertThat(carlosComments).hasSize(2);
        carlosComments.forEach(comment -> assertThat(comment.getAuthorDetails().getUsername()).isEqualTo(carlo.getUsername()));

        assertThat(mariosComments).hasSize(2);
        mariosComments.forEach(comment -> assertThat(comment.getAuthorDetails().getUsername()).isEqualTo(mario.getUsername()));

        assertThat(francoComments).isEmpty();
    }

}
