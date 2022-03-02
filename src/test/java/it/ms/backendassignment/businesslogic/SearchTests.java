package it.ms.backendassignment.businesslogic;

import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Post;
import it.ms.backendassignment.repository.PostRepository;
import it.ms.backendassignment.service.PostService;
import it.ms.backendassignment.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private PostRepository postRepository;

    @BeforeEach
    void nukeDb() {
        postRepository.deleteAll();
    }

    @Test
    void shouldFindPostsWithKeywordInTitle() throws BAException {
        List<PostDto> postList = List.of(new PostDto("Title 1", "Body 1"),
                new PostDto("Title 2", "Body 2"),
                new PostDto("Title 3", "Body 3"),
                new PostDto("Hello again", "World 4"),
                new PostDto("Title 5", "Body 5"),
                new PostDto("Title 6", "Body 6"),
                new PostDto("Hello 7", "Hi"),
                new PostDto("Hello", "World"));

        for (PostDto post : postList) {
            postService.createPost(post);
        }

        String keyword = "hello";
        List<Post> posts = searchService.searchPostsByKeyword(0, 5, keyword);

        assertThat(posts).hasSize(3);
        assertThat(posts.stream().filter(post -> StringUtils.containsIgnoreCase(post.getTitle(), keyword))).hasSize(3);
    }

    @Test
    void shouldFindPostsWithKeywordInBody() throws BAException {
        List<PostDto> postList = List.of(new PostDto("Title 1", "Body 1"),
                new PostDto("Title 2", "Body 2"),
                new PostDto("Title 3", "Body 3"),
                new PostDto("Hello again", "World 4"),
                new PostDto("Title 5", "Body 5"),
                new PostDto("Title 6", "Body 6"),
                new PostDto("Hello 7", "Hi"),
                new PostDto("Hello", "World"));

        for (PostDto post : postList) {
            postService.createPost(post);
        }

        String keyword = "hi";
        List<Post> posts = searchService.searchPostsByKeyword(0, 5, keyword);

        assertThat(posts).hasSize(1);
        assertThat(posts.stream().filter(post -> StringUtils.containsIgnoreCase(post.getBody(), keyword))).hasSize(1);
    }

    @Test
    void shouldFindPostsWithKeywordInTitleAndBody() throws BAException {
        List<PostDto> postList = List.of(
                new PostDto("Hello", "Body"),
                new PostDto("Word", "Hello"),
                new PostDto("Algorithms", "Data"),
                new PostDto("Another title", "Another body")
        );

        for (PostDto post : postList) {
            postService.createPost(post);
        }

        String keyword = "hello";
        List<Post> posts = searchService.searchPostsByKeyword(0, 5, keyword);

        assertThat(posts).hasSize(2);
        assertThat(posts.stream().filter(post -> StringUtils.containsIgnoreCase(post.getTitle(), keyword) ||
                StringUtils.containsIgnoreCase(post.getBody(), keyword))).hasSize(2);
    }

    @Test
    void shouldReturnEmptyListWhenNoMatches() throws BAException {
        List<PostDto> postList = List.of(new PostDto("Title 1", "Body 1"),
                new PostDto("Title 2", "Body 2"),
                new PostDto("Title 3", "Body 3"),
                new PostDto("Hello again", "World 4"),
                new PostDto("Title 5", "Body 5"),
                new PostDto("Title 6", "Body 6"),
                new PostDto("Hello 7", "Hi"),
                new PostDto("Hello", "World"));

        for (PostDto post : postList) {
            postService.createPost(post);
        }

        String keyword = "ciao";
        List<Post> posts = searchService.searchPostsByKeyword(0, 5, keyword);

        assertThat(posts).isEmpty();
    }

}
