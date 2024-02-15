package com.turingSecApp.turingSec.controller;


import com.turingSecApp.turingSec.Request.LoginRequest;
import com.turingSecApp.turingSec.dao.entities.AdminEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import com.turingSecApp.turingSec.dao.repository.RoleRepository;
import com.turingSecApp.turingSec.dao.repository.UserRepository;
import com.turingSecApp.turingSec.exception.UserNotActivatedException;
import com.turingSecApp.turingSec.filter.JwtUtil;
import com.turingSecApp.turingSec.service.user.CustomUserDetails;
import com.turingSecApp.turingSec.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JwtUtil jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;



    @Autowired
    private PasswordEncoder passwordEncoder;




    @PostMapping("/register/hacker")
    public ResponseEntity<?> registerHacker(@RequestBody UserEntity user) {
        ResponseEntity<?> registeredUser = userService.registerHacker(user);

        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserEntity> getCurrentUser(@AuthenticationPrincipal User user) {
        // Retrieve the currently authenticated user from the security context
        UserEntity currentUser = userRepository.findByUsername(user.getUsername());

        return ResponseEntity.ok(currentUser);
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long userId) {
        // Retrieve user information by ID
        Optional<UserEntity> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        boolean activationResult = userService.activateAccount(token);
        if (activationResult) {
            return new ResponseEntity<>("Account activated successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid activation token.", HttpStatus.BAD_REQUEST);
        }
    }


//    @PostMapping("/register/admin")
//    public ResponseEntity<UserEntity> registerAdmin(@RequestBody UserEntity user) {
//        user.setRoles(Collections.singleton(roleRepository.findByName("ADMIN")));
//        UserEntity registeredUser = userService.registerAdmin(user);
//        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
//    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequest user) {
        // Check if the input is an email
        UserEntity userEntity = userRepository.findByEmail(user.getUsernameOrEmail());

        // If the input is not an email, check if it's a username
        if (userEntity == null) {
            userEntity = userRepository.findByUsername(user.getUsernameOrEmail());
        }

        // Authenticate user if found
        if (userEntity != null && passwordEncoder.matches(user.getPassword(), userEntity.getPassword())) {
            // Check if the user is activated
            if (!userEntity.isActivated()) {
                throw new UserNotActivatedException("User is not activated yet.");
            }

            // Generate token using the user details
            UserDetails userDetails = new CustomUserDetails(userEntity);
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



    @GetMapping("/test")
    public String test() {

        return "test passed";
    }






    @PostMapping("/register/company")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyEntity company) {
        // Register the company with pending approval
        ResponseEntity<?> registeredCompany = userService.registerCompany(company);
        return new ResponseEntity<>(registeredCompany, HttpStatus.CREATED);
    }
}
