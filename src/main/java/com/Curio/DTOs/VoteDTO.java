package com.Curio.DTOs;

import lombok.Data;

@Data
public class VoteDTO {
    private Long userId;
    private Long quesId;
    private Long ansId;
    private boolean vote;
}
