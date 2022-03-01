package it.ms.backendassignment.dto;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class CommentDto {
    private Long postId;
    private String text;
}
