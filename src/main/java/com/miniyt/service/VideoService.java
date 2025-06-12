package com.miniyt.service;

import com.miniyt.dto.response.VideoResponse;
import com.miniyt.model.Video;
import com.miniyt.repository.VideoRepo;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {

    private final VideoRepo videoRepo;

    public VideoService(VideoRepo videoRepo) {
        this.videoRepo = videoRepo;
    }

    public List<VideoResponse> search() {
        List<Video> videos = videoRepo.findAll();
        return videos.stream()
                .map(video -> new VideoResponse(
                        video.getId(),
                        video.getTitle(),
                        video.getDescription(),
                        video.getThumbnail() != null ? Base64.getEncoder().encodeToString(video.getThumbnail()) : ""
                ))
                .collect(Collectors.toList());
    }
}
