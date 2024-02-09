package com.turingSecApp.turingSec.controller;

import com.turingSecApp.turingSec.Request.LoginRequest;
import com.turingSecApp.turingSec.dao.entities.AdminEntity;
import com.turingSecApp.turingSec.filter.JwtUtil;
import com.turingSecApp.turingSec.service.AdminService;
import com.turingSecApp.turingSec.service.CompanyService;
import com.turingSecApp.turingSec.service.user.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

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
        boolean approvalResult = companyService.approveCompanyRegistration(companyId);
        if (approvalResult) {
            return ResponseEntity.ok("Company registration approved successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to approve company registration.");
        }
    }



    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginAdmin(@RequestBody LoginRequest user) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Retrieve the authenticated user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate token using the userDetails
        String token = jwtTokenProvider.generateToken(userDetails);

        // Retrieve the user ID from CustomUserDetails
        Long userId = null;
        if (userDetails instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) userDetails).getId();
        }

        // Create a response map containing the token and user ID
        Map<String, String> response = new HashMap<>();
        response.put("access_token", token);
        response.put("userId", String.valueOf(userId));

        return ResponseEntity.ok(response);
    }

}
