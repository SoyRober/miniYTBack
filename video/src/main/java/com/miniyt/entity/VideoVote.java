package com.miniyt.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "videoVotes")
@AllArgsConstructor
@NoArgsConstructor
@Data
@CompoundIndex(name = "videoId_userId_unique_idx", def = "{'videoId': 1, 'userId': 1}", unique = true)
public class VideoVote {
    public enum Vote {
        UPVOTE,
        DOWNVOTE
    }

    @Id
    private String id;

    private String videoId;

    private String userId;

    private Vote vote;
}
