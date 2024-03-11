package com.turingSecApp.turingSec.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class HackerResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore

    private Long id;
    private String first_name;
    private String last_name;
    private String country;
    private String website;
    private String background_pic;
    private String profile_pic;
    private String bio;
    private String linkedin;
    private String twitter;
    private String github;
    private String city;
    private UserResponse user;
    private Long backgroundImageId;
    private Long imageId;
}
