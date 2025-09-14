package com.Curio.DTOs;

import lombok.Data;

@Data
public class EditQuestionDTO {
    private Long Qid;
    private Long userId;
    private String title;
    private String body;
}
