package com.turingSecApp.turingSec.controller;


import com.turingSecApp.turingSec.Request.LoginRequest;
import com.turingSecApp.turingSec.Request.UserUpdateRequest;
import com.turingSecApp.turingSec.dao.entities.AdminEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.entities.HackerEntity;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import com.turingSecApp.turingSec.dao.repository.HackerRepository;
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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    private HackerRepository hackerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;




    @PostMapping("/register/hacker")
    public ResponseEntity<?> registerHacker(@RequestBody UserEntity user) {
        ResponseEntity<?> registeredUser = userService.registerHacker(user);

        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/current-user")
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            // Retrieve user details from the database
            return userRepository.findByUsername(username);
        } else {
            // Handle case where user is not authenticated
            // You might return an error response or throw an exception
            return null;
        }
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




    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserUpdateRequest profileUpdateRequest) {
        // Get the authenticated user details from the security context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Extract the username from the authenticated user details
        String username = userDetails.getUsername();

        // Retrieve the user entity from the repository based on the username
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            // Handle case where user is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Update the user's first name and last name with the new values
        userEntity.setUsername(profileUpdateRequest.getUsername());

        userEntity.setFirst_name(profileUpdateRequest.getFirst_name());
        userEntity.setLast_name(profileUpdateRequest.getLast_name());
        userEntity.setCountry(profileUpdateRequest.getCountry());

        // Save the updated user entity
        userRepository.save(userEntity);

        // Update the corresponding HackerEntity if it exists
        HackerEntity hackerEntity = hackerRepository.findByUser(userEntity);
        if (hackerEntity != null) {
            hackerEntity.setFirst_name(profileUpdateRequest.getFirst_name());
            hackerEntity.setLast_name(profileUpdateRequest.getLast_name());
            hackerEntity.setCountry(profileUpdateRequest.getCountry());
            hackerEntity.setCity(profileUpdateRequest.getCity());

            hackerEntity.setWebsite(profileUpdateRequest.getWebsite());
            hackerEntity.setBackground_pic(profileUpdateRequest.getBackground_pic());
            hackerEntity.setProfile_pic(profileUpdateRequest.getProfile_pic());
            hackerEntity.setBio(profileUpdateRequest.getBio());
            hackerEntity.setLinkedin(profileUpdateRequest.getLinkedin());
            hackerEntity.setTwitter(profileUpdateRequest.getTwitter());
            hackerEntity.setGithub(profileUpdateRequest.getGithub());

            hackerRepository.save(hackerEntity);
        }

        return ResponseEntity.ok("Profile updated successfully.");
    }


}
