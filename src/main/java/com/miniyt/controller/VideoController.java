package com.miniyt.controller;

import com.miniyt.dto.response.ApiResponse;
import com.miniyt.service.VideoService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @GetMapping("/public/video/stream/{videopath}")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable String videopath,
            @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {

        File videoFile = new File("upload/videos/" + videopath);
        long fileLength = videoFile.length();
        long rangeStart = 0;
        long rangeEnd = fileLength - 1;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.substring(6).split("-");
            rangeStart = Long.parseLong(ranges[0]);
            if (ranges.length > 1 && !ranges[1].isEmpty()) {
                rangeEnd = Long.parseLong(ranges[1]);
            }
        }

        long contentLength = rangeEnd - rangeStart + 1;
        InputStream inputStream = new FileInputStream(videoFile);
        inputStream.skip(rangeStart);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "video/mkv");
        headers.add("Accept-Ranges", "bytes");
        headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);

        return ResponseEntity.status(rangeHeader == null ? 200 : 206)
                .headers(headers)
                .contentLength(contentLength)
                .body(new InputStreamResource(inputStream));
    }
}
