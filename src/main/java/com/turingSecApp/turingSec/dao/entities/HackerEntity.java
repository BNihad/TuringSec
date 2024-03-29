package com.turingSecApp.turingSec.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "hackers")
public class HackerEntity {
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


    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
