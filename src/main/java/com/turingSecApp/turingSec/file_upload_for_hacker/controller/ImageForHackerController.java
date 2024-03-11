package com.turingSecApp.turingSec.file_upload_for_hacker.controller;

import com.turingSecApp.turingSec.file_upload_for_hacker.response.ImageForHackerResponse;
import com.turingSecApp.turingSec.file_upload_for_hacker.service.ImageForHackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/image-for-hacker")
@RequiredArgsConstructor
public class ImageForHackerController {

    private final ImageForHackerService imageForHackerService;


    @PostMapping("/upload")
    public ImageForHackerResponse uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("hackerId") Long hackerId) throws IOException {
        return imageForHackerService.saveVideo(file, hackerId);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadVideo(@PathVariable Long id) throws FileNotFoundException {
        return imageForHackerService.getVideoById(id);
    }

}