package com.miniyt.controller;

import com.miniyt.dto.request.LoginRequest;
import com.miniyt.dto.request.RegisterRequest;
import com.miniyt.dto.response.ApiResponse;
import com.miniyt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private UserService userService;

    @PostMapping("/public/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(
                new ApiResponse(
                        userService.login(loginRequest),
                        true
                )
        );
    }

    @PostMapping("/public/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(
                new ApiResponse(
                        userService.register(registerRequest),
                        true
                )
        );
    }
}
