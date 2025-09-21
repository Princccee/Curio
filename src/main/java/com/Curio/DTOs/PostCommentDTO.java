package com.Curio.DTOs;

import lombok.Data;

@Data
public class PostCommentDTO {
    private Long quesId;
    private Long ansId;
    private Long userId;
    private String comment;
}
