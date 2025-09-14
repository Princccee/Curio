package com.Curio.DTOs;

import lombok.Data;

@Data
public class UpdateProfileDTO {
    private Long userId;
    private String name;
    private String email;
    private String bio;
}
