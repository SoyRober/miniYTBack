package com.miniyt.controller;

import com.miniyt.dto.response.ApiResponse;
import com.miniyt.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/public/search")
    public ResponseEntity<ApiResponse> search() {
        return ResponseEntity.ok(
                new ApiResponse(
                        videoService.search(),
                        true
                )
        );
    }

    @PostMapping("/user/video/upload")
    public ResponseEntity<ApiResponse> uploadUserVideo(@RequestParam("video") MultipartFile videoFile, Principal user)
            throws IOException {
        return ResponseEntity.ok(
                new ApiResponse(videoService.upload(videoFile, user),
                        true
                )
        );
    }
}
