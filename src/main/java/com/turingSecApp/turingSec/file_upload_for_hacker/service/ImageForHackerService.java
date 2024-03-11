package com.turingSecApp.turingSec.file_upload_for_hacker.service;

import com.turingSecApp.turingSec.file_upload_for_hacker.entity.ImageForHacker;
import com.turingSecApp.turingSec.file_upload_for_hacker.repository.ImageForHackerRepository;
import com.turingSecApp.turingSec.file_upload_for_hacker.response.ImageForHackerResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageForHackerService {

    private final ImageForHackerRepository imageForHackerRepository;
    private final ModelMapper modelMapper;

    public ImageForHackerResponse saveVideo(MultipartFile multipartFile, Long hackerId) throws IOException {
        Optional<ImageForHacker> existingFileOptional = imageForHackerRepository.findImageForHackerByHackerId(hackerId);

        if (existingFileOptional.isPresent()) {
            ImageForHacker existingFile = existingFileOptional.get();
            existingFile.setName(multipartFile.getOriginalFilename());
            existingFile.setContentType(multipartFile.getContentType());
            existingFile.setFileData(multipartFile.getBytes());
            ImageForHacker saved = imageForHackerRepository.save(existingFile);
            ImageForHackerResponse response = modelMapper.map(saved, ImageForHackerResponse.class);
            return response;
        } else {
            ImageForHacker file = new ImageForHacker();
            file.setName(multipartFile.getOriginalFilename());
            file.setContentType(multipartFile.getContentType());
            file.setFileData(multipartFile.getBytes());
            file.setHackerId(hackerId);
            ImageForHacker saved = imageForHackerRepository.save(file);
            ImageForHackerResponse response = modelMapper.map(saved, ImageForHackerResponse.class);
            return response;
        }
    }


    public ResponseEntity<?> getVideoById(Long id) throws FileNotFoundException {
        ImageForHacker fileOptional = imageForHackerRepository.findById(id).orElseThrow(
                () -> new FileNotFoundException("File not found"));
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", fileOptional.getContentType())
                .body(fileOptional.getFileData());
    }

}