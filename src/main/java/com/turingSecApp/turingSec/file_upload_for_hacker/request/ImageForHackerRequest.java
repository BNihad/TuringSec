package com.turingSecApp.turingSec.file_upload_for_hacker.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageForHackerRequest {

    private String name;
    private String contentType;
    private byte[] fileData;

}