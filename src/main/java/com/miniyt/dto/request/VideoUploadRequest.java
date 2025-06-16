package com.miniyt.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class VideoUploadRequest {
    @NotNull
    private String title;

    private String description;

    private String thumbnail;

    @NotNull
    private MultipartFile file;

    public VideoUploadRequest(String title, String description, String thumbnail, MultipartFile file) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
