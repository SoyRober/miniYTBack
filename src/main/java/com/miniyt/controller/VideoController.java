package com.miniyt.controller;

import com.miniyt.dto.response.ApiResponse;
import com.miniyt.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
