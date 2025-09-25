package com.Curio.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDTO {
    private String email;
    private String name;
    private String password;
    private String bio;
}
