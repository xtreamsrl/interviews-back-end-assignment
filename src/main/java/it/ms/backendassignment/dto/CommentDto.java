package it.ms.backendassignment.dto;

import it.ms.backendassignment.domain.Comment;
import it.ms.backendassignment.model.AuthorDetails;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long postId;
    private String text;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private AuthorDetails authorDetails = new AuthorDetails();

    public CommentDto(Comment comment) {
        BeanUtils.copyProperties(comment, this);
        this.postId = comment.getPost().getId();
        this.authorDetails.setUsername(comment.getUser().getUsername());
        this.authorDetails.setUserId(comment.getUser().getId());
    }
}
