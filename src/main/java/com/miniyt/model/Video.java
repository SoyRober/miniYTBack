package com.miniyt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition="TEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] thumbnail;

    @Column(name = "video_path")
    private String videoPath;

    private String uuid;

    @ManyToOne
    private User user;
}
