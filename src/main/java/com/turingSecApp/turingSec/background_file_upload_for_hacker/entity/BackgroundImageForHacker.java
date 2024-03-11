package com.turingSecApp.turingSec.background_file_upload_for_hacker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "background_image_for_hacker")
public class BackgroundImageForHacker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String contentType;

    @Column(columnDefinition = "BYTEA")
    private byte[] fileData;

    private Long hackerId;
}