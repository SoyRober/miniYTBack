package com.miniyt.controller;

import com.miniyt.dto.request.LoginRequest;
import com.miniyt.dto.request.RegisterRequest;
import com.miniyt.dto.response.ApiResponse;
import com.miniyt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(
                new ApiResponse(
                        userService.login(loginRequest),
                        true
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest registerRequest) {
        log.info("Entrando");
        return ResponseEntity.ok(
                new ApiResponse(
                        userService.register(registerRequest),
                        true
                )
        );
    }
}
