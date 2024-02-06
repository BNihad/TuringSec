package com.turingSecApp.turingSec.service.user;


import com.turingSecApp.turingSec.dao.entities.role.Role;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails extends User {

    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        super(user.getUsername(), user.getPassword(), getAuthorities(user.getRoles()));
        this.user = user;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        // Convert roles to a list of GrantedAuthority objects
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    public UserEntity getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }
}
