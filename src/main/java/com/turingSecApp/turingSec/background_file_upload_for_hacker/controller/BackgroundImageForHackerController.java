package com.turingSecApp.turingSec.background_file_upload_for_hacker.controller;

import com.turingSecApp.turingSec.background_file_upload_for_hacker.entity.BackgroundImageForHacker;
import com.turingSecApp.turingSec.background_file_upload_for_hacker.response.FileResponse;
import com.turingSecApp.turingSec.background_file_upload_for_hacker.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/background-image-for-hacker")
@RequiredArgsConstructor
public class BackgroundImageForHackerController {

    private final FileService fileService;

    @PostMapping("/upload")
    public FileResponse uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("hackerId")Long hackerId) throws IOException {
        return fileService.saveVideo(file, hackerId);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadVideo(@PathVariable Long id) throws FileNotFoundException {
        return fileService.getVideoById(id);
    }

}