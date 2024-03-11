package com.turingSecApp.turingSec.background_file_upload_for_hacker.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileRequest {

    private String name;
    private String contentType;
    private byte[] fileData;

}