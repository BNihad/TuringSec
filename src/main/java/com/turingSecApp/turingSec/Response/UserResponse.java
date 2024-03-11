package com.turingSecApp.turingSec.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turingSecApp.turingSec.dao.entities.HackerEntity;
import com.turingSecApp.turingSec.dao.entities.role.Role;
import jakarta.persistence.*;

import java.util.Set;

public class UserResponse {
    private Long id;
    private String first_name;
    private String last_name;
    private String country;
    private String username;
    private String password;
    private String email;
    private String activationToken;
    private boolean activated;
    private Set<Role> roles;
    private HackerResponse hackerResponse;
}
