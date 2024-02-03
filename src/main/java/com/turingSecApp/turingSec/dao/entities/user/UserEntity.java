package com.turingSecApp.turingSec.dao.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.turingSecApp.turingSec.dao.entities.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String first_name;
    private String last_name;
    private String country;


    private String username;

    private String password;
    private String email;


    @Column(name = "activation_token")
    private String activationToken;

    @Column(name = "activated")
    private boolean activated;



    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    private Set<Role> roles;



}
