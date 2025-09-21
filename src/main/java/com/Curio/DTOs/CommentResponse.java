package com.Curio.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {
    private Long commentId;
    private Long quesId;
    private Long ansId;
    private String quesTitle;
    private String answer;
    private String comment;
}
