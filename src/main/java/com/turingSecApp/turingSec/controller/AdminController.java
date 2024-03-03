package com.turingSecApp.turingSec.controller;

import com.turingSecApp.turingSec.Request.LoginRequest;
import com.turingSecApp.turingSec.dao.entities.AdminEntity;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import com.turingSecApp.turingSec.dao.repository.AdminRepository;
import com.turingSecApp.turingSec.filter.JwtUtil;
import com.turingSecApp.turingSec.service.AdminService;
import com.turingSecApp.turingSec.service.CompanyService;
import com.turingSecApp.turingSec.service.user.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CompanyService companyService;


    @Autowired
    private JwtUtil jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminEntity admin) {
        ResponseEntity<?> registerAdmin = adminService.registerAdmin(admin);
        return new ResponseEntity<>(registerAdmin, HttpStatus.CREATED);
    }
    
    // Other admin management endpoints


    @PostMapping("/approve-company/{companyId}")
    public ResponseEntity<?> approveCompanyRegistration(@PathVariable Long companyId) {
        // Assuming you have a method in the CompanyService to approve company registration
        String generatedPassword = companyService.approveCompanyRegistration(companyId);
        if (generatedPassword != null) {
            return ResponseEntity.ok("Company registration approved successfully. Generated password: " + generatedPassword);
        } else {
            return ResponseEntity.badRequest().body("Failed to approve company registration.");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginAdmin(@RequestBody LoginRequest user) {
        // Check if the input is an email
        AdminEntity adminEntity = adminRepository.findByEmail(user.getUsernameOrEmail());

        // If the input is not an email, check if it's a username
        if (adminEntity == null) {
            adminEntity = adminRepository.findByUsername(user.getUsernameOrEmail());
        }

        // Authenticate user if found
        if (adminEntity != null && passwordEncoder.matches(user.getPassword(), adminEntity.getPassword())) {
            // Generate token using the user details
            UserDetails userDetails = new CustomUserDetails(adminEntity);
            String token = jwtTokenProvider.generateToken(userDetails);

            // Retrieve the user ID from CustomUserDetails
            Long userId = ((CustomUserDetails) userDetails).getId();

            // Create a response map containing the token and user ID
            Map<String, String> response = new HashMap<>();
            response.put("access_token", token);
            response.put("userId", String.valueOf(userId));

            return ResponseEntity.ok(response);
        } else {
            // Authentication failed
            throw new BadCredentialsException("Invalid username/email or password.");
        }
    }

}
