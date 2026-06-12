package com.server.app.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordDto {

    @NotBlank
    private String oldpassword;

    @NotBlank
    private String newpassword;

    @NotBlank
    private String confirmpassword;
}