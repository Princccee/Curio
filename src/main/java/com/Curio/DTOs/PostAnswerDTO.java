package com.Curio.DTOs;

import lombok.Data;

@Data
public class PostAnswerDTO {
    private Long quesId;
    private Long userId;
    private String answer;
}
