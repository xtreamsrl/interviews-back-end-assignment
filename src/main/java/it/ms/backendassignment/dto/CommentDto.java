package it.ms.backendassignment.dto;

import it.ms.backendassignment.model.AuthorDetails;
import it.ms.backendassignment.model.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long postId;
    private String text;
    private AuthorDetails authorDetails = new AuthorDetails();

    public CommentDto(Comment comment) {
        BeanUtils.copyProperties(comment, this);
        this.authorDetails.setUsername(comment.getUser().getUsername());
        this.authorDetails.setUserId(comment.getUser().getId());
    }
}
