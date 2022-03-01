package it.ms.backendassignment.crud;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.dto.DeletePostDto;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Post;
import it.ms.backendassignment.repository.PostRepository;
import it.ms.backendassignment.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class PostTests {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void nukeDb() {
        postRepository.deleteAll();
    }

    @Test
    void shouldNotPersistBadPosts() {
        PostDto postWithNoTitle = new PostDto();
        postWithNoTitle.setBody("This should not be saved");

        PostDto postWithNoBody = new PostDto();
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
        PostDto postIn = new PostDto();
        postIn.setTitle("A pretty title");
        postIn.setBody("A pretty body");

        try {
            Post out = postService.createPost(postIn);

            assertThat(out.getId()).isNotNull();
            assertThat(out.getTitle()).isEqualTo(postIn.getTitle());
            assertThat(out.getBody()).isEqualTo(postIn.getBody());
        } catch (BAException e) {
            fail("Shouldn't have thrown BAException");
        }
    }

    @Test
    void shouldGetPostById() throws BAException {
        PostDto postIn = new PostDto();
        postIn.setTitle("A pretty title");
        postIn.setBody("A pretty body");
        Post out = postService.createPost(postIn);

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
        List<PostDto> postList = List.of(new PostDto("Title 1", "Body 1"),
                                         new PostDto("Title 2", "Body 2"),
                                         new PostDto("Title 3", "Body 3"),
                                         new PostDto("Title 4", "Body 4"),
                                         new PostDto("Title 5", "Body 5"),
                                         new PostDto("Title 6", "Body 6"),
                                         new PostDto("Title 7", "Body 7"));

        for (PostDto post : postList) {
            postService.createPost(post);
            //sleep for 1 second to have different creation dates
            TimeUnit.SECONDS.sleep(1);
        }

        List<Post> firstPage = postService.getPosts(0, 5);
        List<Post> secondPage = postService.getPosts(1, 5);

        assertThat(firstPage).hasSize(5);
        assertThat(secondPage).hasSize(2);

        //checks that the first item is the most recent one
        Post mostRecentPost = firstPage.get(0);

        for (int i = 1, firstPageSize = firstPage.size(); i < firstPageSize; i++) {
            Post post = firstPage.get(i);
            assertThat(mostRecentPost
                    .getCreationDate()
                    .isAfter(post.getCreationDate())
            ).isTrue();
        }
    }

    @Test
    void shouldDeletePost() throws BAException {
        PostDto postIn = new PostDto();
        postIn.setTitle("A pretty title");
        postIn.setBody("A pretty body");
        Post out = postService.createPost(postIn);

        DeletePostDto deletePostDto = postService.deletePost(out.getId());

        assertThat(deletePostDto.getSuccess()).isTrue();
    }
    @Test
    void shouldntDeletePost() {
        DeletePostDto deletePostDto = postService.deletePost(2L);
        assertThat(deletePostDto.getSuccess()).isFalse();
    }

    @Test
    void shouldUpdatePost() throws BAException {
        PostDto postIn = new PostDto();
        postIn.setTitle("A pretty title");
        postIn.setBody("A pretty body");
        Post out = postService.createPost(postIn);

        PostDto newPost = new PostDto();

        newPost.setTitle("An even prettier title");

        Post editedPost = postService.editPost(out.getId(), newPost);

        assertThat(editedPost.getTitle()).isEqualTo("An even prettier title");
        assertThat(editedPost.getBody()).isEqualTo(out.getBody());
    }
}
