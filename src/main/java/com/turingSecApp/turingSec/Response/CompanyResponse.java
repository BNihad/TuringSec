package com.turingSecApp.turingSec.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turingSecApp.turingSec.dao.entities.BugBountyProgramEntity;
import com.turingSecApp.turingSec.dao.entities.role.Role;
import com.turingSecApp.turingSec.dao.entities.role.UserRoles;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
@Data
public class CompanyResponse {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String company_name;
    private String job_title;
    private String assets;
    private String message;
    private String password;

    private boolean approved; // Indicates whether the company registration is approved

    private Set<Role> roles;

    private Set<BugBountyProgramEntity> bugBountyPrograms;

    private Set<UserRoles> userRoles;

    private Long fileId;
}
