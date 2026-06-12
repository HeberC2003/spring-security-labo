package com.server.app.dto.auth;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpDto {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Size(min = 2, max = 50)
    private String surname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$",
            message = "La contraseña debe tener mínimo 8 caracteres, mayúscula, minúscula, número y carácter especial")
    private String password;
}