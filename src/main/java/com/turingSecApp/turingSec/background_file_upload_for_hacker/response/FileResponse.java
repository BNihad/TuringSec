package com.turingSecApp.turingSec.background_file_upload_for_hacker.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponse {

    private Long id;

    private String name;
    private String contentType;
    private Long hackerId;

    private byte[] fileData;

}