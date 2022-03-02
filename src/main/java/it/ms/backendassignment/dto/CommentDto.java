package it.ms.backendassignment.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long postId;
    private String text;
}
