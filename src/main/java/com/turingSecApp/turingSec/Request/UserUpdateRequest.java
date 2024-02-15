package com.turingSecApp.turingSec.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateRequest {

    private String username;
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

}
