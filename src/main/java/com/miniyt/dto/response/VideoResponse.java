package com.miniyt.dto.response;

public class VideoResponse {
    private String uuid;

    private String title;

    private String description;

    private String thumbnail;

    public VideoResponse(String uuid, String title, String description, String thumbnail) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
