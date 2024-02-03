package com.turingSecApp.turingSec.controller;


import com.turingSecApp.turingSec.Request.LoginRequest;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import com.turingSecApp.turingSec.dao.repository.RoleRepository;
import com.turingSecApp.turingSec.dao.repository.UserRepository;
import com.turingSecApp.turingSec.filter.JwtUtil;
import com.turingSecApp.turingSec.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;

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


    @PostMapping("/register/admin")
    public ResponseEntity<UserEntity> registerAdmin(@RequestBody UserEntity user) {
        user.setRoles(Collections.singleton(roleRepository.findByName("ADMIN")));
        UserEntity registeredUser = userService.registerAdmin(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(token);
    }
}
