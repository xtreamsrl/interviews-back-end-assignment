package it.ms.backendassignment.dto;

import lombok.Data;

@Data
public class CommentDtoIn {
    private Long postId;
    private String text;
    private String username;
}
