package com.Curio.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerResponse {
    private Long quesId;
    private Long userId;
    private String title;
    private String body;
    private String ans;
}
