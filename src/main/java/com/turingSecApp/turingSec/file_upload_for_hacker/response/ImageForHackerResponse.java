package com.turingSecApp.turingSec.file_upload_for_hacker.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageForHackerResponse {

    private Long id;

    private String name;
    private String contentType;
    private Long hackerId;

    private byte[] fileData;


}