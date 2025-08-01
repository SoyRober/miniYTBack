package com.miniyt.controller;

import com.miniyt.dto.request.VideoUploadRequest;
import com.miniyt.dto.response.ApiResponse;
import com.miniyt.dto.response.VideoResponse;
import com.miniyt.entity.Video;
import com.miniyt.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

@RestController
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/public/search")
    public ResponseEntity<ApiResponse> search(@RequestParam(required = false) String search, @RequestParam(defaultValue = "0") int page) {
        List<VideoResponse> videos = videoService.search(search, page);

        return videos.isEmpty() ?
                ResponseEntity.ok(new ApiResponse("No videos found", false)) :
                ResponseEntity.ok(new ApiResponse(videos, true));
    }

    @PostMapping(value = "/user/video/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploadUserVideo(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") @Valid VideoUploadRequest videoUploadRequest,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            Principal user) throws IOException {
        return ResponseEntity.ok(
                new ApiResponse(videoService.upload(file, videoUploadRequest, thumbnail, user), true)
        );
    }

    @GetMapping("/public/video/{uuid}")
    public ResponseEntity<ApiResponse> getVideo(@PathVariable String uuid) {
        return ResponseEntity.ok(
                new ApiResponse(videoService.getVideo(uuid), true)
        );
    }

    @GetMapping("/public/video/stream/{uuid}")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable String uuid,
            @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {

        Video currentVideo = videoService.getVideoByUuid(uuid);

        File videoFile = new File(currentVideo.getVideoPath());
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
        headers.add("Content-Type", "video/mp4");
        headers.add("Accept-Ranges", "bytes");
        headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);

        return ResponseEntity.status(rangeHeader == null ? 200 : 206)
                .headers(headers)
                .contentLength(contentLength)
                .body(new InputStreamResource(inputStream));
    }
}
