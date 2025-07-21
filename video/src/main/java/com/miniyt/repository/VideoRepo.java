package com.miniyt.repository;

import com.miniyt.entity.Video;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepo extends JpaRepository<Video, Long> {
    Optional<Video> findByUuid(String uuid);

    List<Video> findByTitleContainingIgnoreCase(String searchTerm, PageRequest pageRequest);
}
