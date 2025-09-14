package com.Curio.DTOs;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String name;
    private String password;
    private String bio;
}
