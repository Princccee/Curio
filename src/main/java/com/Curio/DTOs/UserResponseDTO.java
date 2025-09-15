package com.Curio.DTOs;

import com.Curio.Models.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserResponseDTO {
    private String name;
    private String email;
    private String bio;
    private Set<Long> followersIds;
    private Set<Long> followingsIds;
    private Set<String> questions;
    private Set<Long> answerIds;
}
