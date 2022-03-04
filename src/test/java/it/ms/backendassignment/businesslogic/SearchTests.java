package it.ms.backendassignment.businesslogic;

import it.ms.backendassignment.domain.User;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.dto.PostDtoIn;
import it.ms.backendassignment.dto.UserDto;
import it.ms.backendassignment.dto.UserSignUpDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.repository.PostRepository;
import it.ms.backendassignment.repository.UserRepository;
import it.ms.backendassignment.service.PostService;
import it.ms.backendassignment.service.SearchService;
import it.ms.backendassignment.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    private PostRepository postRepository;

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

}
