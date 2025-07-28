package com.miniyt.service;

import com.miniyt.dto.request.VideoUploadRequest;
import com.miniyt.dto.response.VideoResponse;
import com.miniyt.exception.NonExistentEntityException;
import com.miniyt.entity.Video;
import com.miniyt.repository.UserRepo;
import com.miniyt.repository.VideoRepo;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepo videoRepo;
    private final UserRepo userRepo;

    public List<VideoResponse> search(String searchTerm, int page) {
        PageRequest pageRequest = PageRequest.of(page, 22);

        List<Video> videos = searchTerm == null || searchTerm.isEmpty() ?
                videoRepo.findAll(pageRequest).getContent() :
                videoRepo.findByTitleContainingIgnoreCase(searchTerm, pageRequest);

        return videos.stream()
                .map(video -> new VideoResponse(
                        video.getId(),
                        video.getUuid(),
                        video.getTitle(),
                        video.getDescription(),
                        video.getThumbnail() != null ? Base64.getEncoder().encodeToString(video.getThumbnail()) : ""
                ))
                .collect(Collectors.toList());
    }

    public String upload(MultipartFile file, VideoUploadRequest videoUploadRequest,
                         MultipartFile thumbnail, Principal user) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String originalFileName = uuid + "_" + videoUploadRequest.getTitle();
        Path originalFilePath = Paths.get("upload/videos/" + originalFileName);
        Files.createDirectories(originalFilePath.getParent());
        Files.write(originalFilePath, file.getBytes());

        String mp4FileName = uuid + ".mp4";
        Path mp4FilePath = Paths.get("upload/videos/" + mp4FileName);

        if (!originalFileName.toLowerCase().endsWith(".mp4")) {
            convertFileToMp4(originalFilePath, mp4FilePath);
        } else {
            Files.move(originalFilePath, mp4FilePath);
        }

        User currentUser = userRepo.findByUsername(user.getName())
                .orElseThrow(() -> new NonExistentEntityException("This user does not exist"));

        Video newVideo = new Video();
        newVideo.setUuid(uuid);
        newVideo.setUser(currentUser);
        newVideo.setThumbnail(thumbnail != null ? thumbnail.getBytes() : null);
        newVideo.setDescription(videoUploadRequest.getDescription());
        newVideo.setTitle(videoUploadRequest.getTitle());
        newVideo.setVideoPath(mp4FilePath.toString());

        videoRepo.save(newVideo);

        return "Video uploaded successfully: " + mp4FileName;
    }

    private static void convertFileToMp4(Path originalFilePath, Path mp4FilePath) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg", "-i", originalFilePath.toString(),
                "-c:v", "libx264", "-c:a", "aac", mp4FilePath.toString()
        );
        pb.inheritIO();
        Process process = pb.start();
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) throw new IOException("Error while converting to mp4");
            Files.delete(originalFilePath);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Conversion interrupted", e);
        }
    }

    public Video getVideoByUuid(String uuid) {
        return videoRepo.findByUuid(uuid)
                .orElseThrow(() -> new NonExistentEntityException("This video does not exist"));
    }

    public VideoResponse getVideo(String uuid) {
        return videoRepo.findByUuid(uuid)
                .map(video -> new VideoResponse(
                        null,
                        video.getUuid(),
                        video.getTitle(),
                        video.getDescription(),
                        null
                ))
                .orElseThrow(() -> new NonExistentEntityException("This video does not exist"));
    }
}
