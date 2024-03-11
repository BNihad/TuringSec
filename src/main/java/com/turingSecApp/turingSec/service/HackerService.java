package com.turingSecApp.turingSec.service;

import com.turingSecApp.turingSec.Response.HackerResponse;
import com.turingSecApp.turingSec.background_file_upload_for_hacker.repository.FileRepository;
import com.turingSecApp.turingSec.dao.entities.HackerEntity;
import com.turingSecApp.turingSec.dao.repository.HackerRepository;
import com.turingSecApp.turingSec.file_upload_for_hacker.repository.ImageForHackerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class HackerService {
    private final ModelMapper modelMapper;
    private final HackerRepository hackerRepository;
    private final FileRepository fileRepository;
    private final ImageForHackerRepository imageForHackerRepository;

    public HackerService(ModelMapper modelMapper, HackerRepository hackerRepository, FileRepository fileRepository, ImageForHackerRepository imageForHackerRepository) {
        this.modelMapper = modelMapper;
        this.hackerRepository = hackerRepository;
        this.fileRepository = fileRepository;
        this.imageForHackerRepository = imageForHackerRepository;
    }

    public ResponseEntity<HackerResponse> findById(Long hackerId){
        HackerEntity hackerEntity = hackerRepository.findById(hackerId).get();
        HackerResponse hackerResponse = modelMapper.map(hackerEntity, HackerResponse.class);
        hackerResponse.setBackgroundImageId(fileRepository.findBackgroundImageForHackerByHackerId(hackerId).get().getId());
        hackerResponse.setImageId(imageForHackerRepository.findImageForHackerByHackerId(hackerId).get().getId());

        return ResponseEntity.ok(hackerResponse);


    }
}
