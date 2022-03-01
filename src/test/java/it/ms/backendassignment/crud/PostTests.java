package it.ms.backendassignment.crud;

import it.ms.backendassignment.constants.Constants;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.exception.BAException;
import it.ms.backendassignment.model.Post;
import it.ms.backendassignment.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class PostTests {

    @Autowired
    private PostService postService;

    @Test
    void shouldNotPersistBadPosts() {
        PostDto postWithNoTitle = new PostDto();
        postWithNoTitle.setBody("This should not be saved");

        PostDto postWithNoBody= new PostDto();
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
}
