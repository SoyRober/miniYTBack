package com.miniyt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponse {
    private Long id;

    private String uuid;

    private String title;

    private String description;

    private String thumbnail;
}
