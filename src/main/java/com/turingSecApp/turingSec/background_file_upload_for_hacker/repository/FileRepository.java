package com.turingSecApp.turingSec.background_file_upload_for_hacker.repository;

import com.turingSecApp.turingSec.background_file_upload_for_hacker.entity.BackgroundImageForHacker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<BackgroundImageForHacker, Long> {
//aa
    Optional<BackgroundImageForHacker> findBackgroundImageForHackerByHackerId(Long hackerId);
    BackgroundImageForHacker findById(long id);
}