package com.turingSecApp.turingSec.service.user;

import com.turingSecApp.turingSec.dao.entities.AdminEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.entities.role.Role;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final Object user;

    public CustomUserDetails(Object user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user instanceof UserEntity) {
            return getAuthoritiesFromRoles(((UserEntity) user).getRoles());
        } else if (user instanceof AdminEntity) {
            return getAuthoritiesFromRoles(((AdminEntity) user).getRoles());
        } else if (user instanceof CompanyEntity) {
            return getAuthoritiesFromRoles(((CompanyEntity) user).getRoles());
        } else {
            throw new IllegalStateException("Unsupported user type: " + user.getClass());
        }
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesFromRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        if (user instanceof UserEntity) {
            return ((UserEntity) user).getPassword();
        } else if (user instanceof AdminEntity) {
            return ((AdminEntity) user).getPassword();
        } else if (user instanceof CompanyEntity) {
            return ((CompanyEntity) user).getPassword();
        } else {
            throw new IllegalStateException("Unsupported user type: " + user.getClass());
        }
    }

    @Override
    public String getUsername() {
        if (user instanceof UserEntity) {
            return ((UserEntity) user).getUsername();
        } else if (user instanceof AdminEntity) {
            return ((AdminEntity) user).getUsername();
        } else if (user instanceof CompanyEntity) {
            return ((CompanyEntity) user).getEmail();
        } else {
            throw new IllegalStateException("Unsupported user type: " + user.getClass());
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement as needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement as needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement as needed
    }

    public Object getUser() {
        return user;
    }

    public Long getId() {
        if (user instanceof UserEntity) {
            return ((UserEntity) user).getId();
        } else if (user instanceof AdminEntity) {
            return ((AdminEntity) user).getId();
        } else if (user instanceof CompanyEntity) {
            return ((CompanyEntity) user).getId();
        } else {
            throw new IllegalStateException("Unsupported user type: " + user.getClass());
        }
    }
}
