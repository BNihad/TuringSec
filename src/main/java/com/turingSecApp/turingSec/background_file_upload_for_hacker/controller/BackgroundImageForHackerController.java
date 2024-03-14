package com.turingSecApp.turingSec.background_file_upload_for_hacker.controller;

import com.turingSecApp.turingSec.background_file_upload_for_hacker.entity.BackgroundImageForHacker;
import com.turingSecApp.turingSec.background_file_upload_for_hacker.response.FileResponse;
import com.turingSecApp.turingSec.background_file_upload_for_hacker.service.FileService;
import com.turingSecApp.turingSec.dao.entities.HackerEntity;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import com.turingSecApp.turingSec.dao.repository.UserRepository;
import com.turingSecApp.turingSec.file_upload_for_hacker.response.ImageForHackerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.NotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/background-image-for-hacker")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class BackgroundImageForHackerController {
    @Autowired
    private UserRepository userRepository;

    private final FileService fileService;

    @PostMapping("/upload")
    public FileResponse uploadVideo(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        // Extract username from the authenticated user details
        String username = userDetails.getUsername();

        // Retrieve the user entity from the repository based on the username
        UserEntity userEntity = userRepository.findByUsername(username);

        // Retrieve the hackerId associated with the user
        Long hackerId = null;
        if (userEntity != null) {
            HackerEntity hackerEntity = userEntity.getHacker();
            if (hackerEntity != null) {
                hackerId = hackerEntity.getId();
            }
        }

        // Check if hackerId is available
        if (hackerId == null) {
            // Handle case where hackerId is not found
            throw new NotFoundException("Hacker ID not found for the authenticated user.");
        }

        // Call the service method to save the video
        return fileService.saveVideo(file, hackerId);
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadVideo(@PathVariable Long id) throws FileNotFoundException {
        return fileService.getVideoById(id);
    }

}