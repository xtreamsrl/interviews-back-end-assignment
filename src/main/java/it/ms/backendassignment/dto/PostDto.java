package it.ms.backendassignment.dto;

import it.ms.backendassignment.model.AuthorDetails;
import it.ms.backendassignment.model.Comment;
import it.ms.backendassignment.model.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Set<CommentDto> comments = new HashSet<>();
    private AuthorDetails authorDetails = new AuthorDetails();

    public PostDto(Post post) {
        BeanUtils.copyProperties(post, this);

        this.authorDetails.setUserId(post.getUser().getId());
        this.authorDetails.setUsername(post.getUser().getUsername());

        for (Comment comment : Optional.of(post).map(Post::getComments).orElse(new HashSet<>())) {
            CommentDto dto = new CommentDto();
            BeanUtils.copyProperties(comment, dto);
            dto.setPostId(comment.getPost().getId());

            AuthorDetails authorDetails = new AuthorDetails();
            authorDetails.setUsername(comment.getUser().getUsername());
            authorDetails.setUserId(comment.getUser().getId());

            dto.setAuthorDetails(authorDetails);

            this.comments.add(dto);
        }

    }
}
