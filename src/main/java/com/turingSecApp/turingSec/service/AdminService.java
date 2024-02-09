package com.turingSecApp.turingSec.service;

import com.turingSecApp.turingSec.dao.entities.AdminEntity;
import com.turingSecApp.turingSec.dao.entities.role.Role;
import com.turingSecApp.turingSec.dao.repository.AdminRepository;
import com.turingSecApp.turingSec.dao.repository.RoleRepository;
import com.turingSecApp.turingSec.exception.EmailAlreadyExistsException;
import com.turingSecApp.turingSec.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Collections;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    PasswordEncoder passwordEncoder;

    public ResponseEntity<?> registerAdmin(AdminEntity admin) {
        // You may want to perform validation or other checks before saving the admin
        // Ensure the user doesn't exist
        if (adminRepository.findByUsername(admin.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username is already taken.");

        }

        if (adminRepository.findByEmail(admin.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Email is already taken.");

        }

        // Set the "ADMIN" role for the admin
        Role adminRole = roleRepository.findByName("ADMIN");
        if (adminRole == null) {
            throw new NotFoundException("Admin role not found.");
        }


        admin.setRoles(Collections.singleton(adminRole));




        admin.setPassword(passwordEncoder.encode(admin.getPassword()));


         adminRepository.save(admin);

        return ResponseEntity.ok(admin);
    }

    // Other admin management methods
}
