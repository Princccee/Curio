package com.Curio.DTOs;

import com.Curio.Models.Tags;
import lombok.Data;
import org.aspectj.apache.bcel.generic.Tag;

import java.util.Set;

@Data
public class PostQuestionDTO {
    private Long userId;
    private String title;
    private String body;
    private Set<String> tags;
}
