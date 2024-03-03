package com.turingSecApp.turingSec.controller;

import com.turingSecApp.turingSec.Request.CompanyRequest;
import com.turingSecApp.turingSec.Request.LoginRequest;
import com.turingSecApp.turingSec.dao.entities.AdminEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.repository.CompanyRepository;
import com.turingSecApp.turingSec.filter.JwtUtil;
import com.turingSecApp.turingSec.service.CompanyService;
import com.turingSecApp.turingSec.service.user.CustomUserDetails;
import com.turingSecApp.turingSec.service.user.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CompanyController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CompanyRepository companyRepository;


    @Autowired
    private JwtUtil jwtTokenProvider;


    @PostMapping("/register")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyEntity companyEntity) {
        ResponseEntity<?> registerCompany = companyService.registerCompany(companyEntity);
        return new ResponseEntity<>(registerCompany, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginCompany(@RequestBody CompanyRequest companyRequest) {
        // Check if the input is an email
        CompanyEntity companyEntity = companyRepository.findByEmail(companyRequest.getEmail());



        // Authenticate user if found
        if (companyEntity != null && passwordEncoder.matches(companyRequest.getPassword(), companyEntity.getPassword())) {
            // Generate token using the user details
            UserDetails userDetails = new CustomUserDetails(companyEntity);
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



    @GetMapping
    public ResponseEntity<List<CompanyEntity>> getAllCompanies() {
        List<CompanyEntity> companyEntities = userService.getAllCompanies();
        return new ResponseEntity<>(companyEntities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyEntity> getCompaniesById(@PathVariable Long id) {
        CompanyEntity companyEntity = userService.getCompaniesById(id);
        return new ResponseEntity<>(companyEntity, HttpStatus.OK);
    }
}
