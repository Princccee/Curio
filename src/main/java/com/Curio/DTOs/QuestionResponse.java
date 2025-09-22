package com.Curio.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionResponse {
    private Long quesId;
    private String title;
    private String body;
}
