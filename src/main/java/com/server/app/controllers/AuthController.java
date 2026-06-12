package com.server.app.controllers;

import com.server.app.config.JsonWebToken;
import com.server.app.dto.auth.LoginDto;
import com.server.app.dto.auth.SignUpDto;
import com.server.app.dto.auth.UpdatePasswordDto;
import com.server.app.dto.user.UserUpdateDto;
import com.server.app.entities.User;
import com.server.app.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JsonWebToken jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
        User user = userService.login(dto);
        String token = jwtUtil.createToken(user);
        return ResponseEntity.ok(buildAuthResponse(token, user));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto dto) {
        User user = userService.signUp(dto);
        String token = jwtUtil.createToken(user);
        return ResponseEntity.ok(buildAuthResponse(token, user));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/profile")
    public ResponseEntity<?> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserUpdateDto dto) {
        User updated = userService.updateProfile(user.getId(), dto);
        String token = jwtUtil.createToken(updated);
        return ResponseEntity.ok(buildAuthResponse(token, updated));
    }

    @PutMapping("/update/password")
    public ResponseEntity<?> updatePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdatePasswordDto dto) {
        User updated = userService.updatePassword(user.getId(), dto);
        return ResponseEntity.ok(updated);
    }

    private Map<String, Object> buildAuthResponse(String token, User user) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("token", token);
        response.put("data", user);
        return response;
    }
}