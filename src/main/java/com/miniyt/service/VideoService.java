package com.miniyt.service;

import com.miniyt.dto.response.VideoResponse;
import com.miniyt.exception.NonExistentEntityException;
import com.miniyt.model.User;
import com.miniyt.model.Video;
import com.miniyt.repository.UserRepo;
import com.miniyt.repository.VideoRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VideoService {

    private final VideoRepo videoRepo;
    private final UserRepo userRepo;

    public VideoService(VideoRepo videoRepo, UserRepo userRepo) {
        this.videoRepo = videoRepo;
        this.userRepo = userRepo;
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

    public String upload(MultipartFile videoFile, Principal user) throws IOException {
        if (videoFile.isEmpty()) throw new NonExistentEntityException("There is no video to upload");

        String fileName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
        Path filePath = Paths.get("upload/videos/" + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, videoFile.getBytes());

        User currentUser = userRepo.findByUsername(user.getName())
                .orElseThrow(() -> new NonExistentEntityException("This user does not exist"));
        System.out.println("user = " + user.getName());

        Video newVideo = new Video();
        newVideo.setUser(currentUser);
        newVideo.setThumbnail("".getBytes());
        newVideo.setDescription("NewVideo");
        newVideo.setTitle("NewVideo");
        newVideo.setVideoPath(filePath.toString());
        
        videoRepo.save(newVideo);
        
        return "Video uploaded successfully";
    }
}
