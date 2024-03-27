package com.turingSecApp.turingSec.controller;


import com.turingSecApp.turingSec.Request.*;
import com.turingSecApp.turingSec.dao.entities.*;
import com.turingSecApp.turingSec.dao.entities.role.Role;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import com.turingSecApp.turingSec.dao.repository.HackerRepository;
import com.turingSecApp.turingSec.dao.repository.RoleRepository;
import com.turingSecApp.turingSec.dao.repository.UserRepository;
import com.turingSecApp.turingSec.exception.*;
import com.turingSecApp.turingSec.filter.JwtUtil;
import com.turingSecApp.turingSec.service.BugBountyReportService;
import com.turingSecApp.turingSec.service.ProgramsService;
import com.turingSecApp.turingSec.service.user.CustomUserDetails;
import com.turingSecApp.turingSec.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    private ProgramsService programsService;


    @Autowired
    private HackerRepository hackerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;




    @PostMapping("/register/hacker")
    public ResponseEntity<Map<String, String>> registerHacker(@RequestBody UserEntity user) {
        // Ensure the user doesn't exist
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Email is already taken.");
        }

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set user roles
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("HACKER"));
        user.setRoles(roles);

        // Save the user
        user = userRepository.save(user);

        // Create and associate a hackerEntity entity
        HackerEntity hackerEntity = new HackerEntity();
        hackerEntity.setUser(user);
        hackerEntity.setFirst_name(user.getFirst_name()); // Set the username in the hackerEntity entity
        hackerEntity.setLast_name(user.getLast_name()); // Set the age in the hackerEntity entity
        hackerEntity.setCountry(user.getCountry()); // Set the age in the hackerEntity entity
        hackerRepository.save(hackerEntity);

        // Send activation email
        userService.sendActivationEmail(user);

        // Generate token for the registered user
        UserDetails userDetails = new CustomUserDetails(user);
        String token = jwtTokenProvider.generateToken(userDetails);

        // Retrieve the user ID from CustomUserDetails
        Long userId = ((CustomUserDetails) userDetails).getId();

        // Create a response map containing the token and user ID
        Map<String, String> response = new HashMap<>();
        response.put("access_token", token);
        response.put("userId", String.valueOf(userId));

        return ResponseEntity.ok(response);
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






    @PostMapping("/change-email")
    public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            UserEntity user = userRepository.findByUsername(username);

            // Validate password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }

            // Update email
            user.setEmail(request.getNewEmail());
            userRepository.save(user);

            return ResponseEntity.ok("Email updated successfully");
        } else {
            // Handle case where user is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }


    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            UserEntity user = userRepository.findByUsername(username);

            // Validate current password
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect current password");
            }

            // Validate new password and confirm new password
            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password and confirm new password do not match");
            }

            // Update password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            return ResponseEntity.ok("Password updated successfully");
        } else {
            // Handle case where user is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }





    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser() {
        // Get the authenticated user's username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Find the user by username
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }

        // Delete the user
        userRepository.delete(user);

        return ResponseEntity.ok("User deleted successfully");
    }



    @GetMapping("/programs")
    public ResponseEntity<List<BugBountyProgramWithAssetTypeDTO>> getAllBugBountyPrograms() {
        List<BugBountyProgramEntity> programs = programsService.getAllBugBountyPrograms();

        // Map BugBountyProgramEntities to BugBountyProgramDTOs
        List<BugBountyProgramWithAssetTypeDTO> programDTOs = programs.stream()
                .map(programEntity -> {
                    BugBountyProgramWithAssetTypeDTO dto = mapToDTO(programEntity);
                    dto.setCompanyId(programEntity.getCompany().getId());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(programDTOs);
    }
    private BugBountyProgramWithAssetTypeDTO mapToDTO(BugBountyProgramEntity programEntity) {
        BugBountyProgramWithAssetTypeDTO dto = new BugBountyProgramWithAssetTypeDTO();
        dto.setId(programEntity.getId());
        dto.setFromDate(programEntity.getFromDate());
        dto.setToDate(programEntity.getToDate());
        dto.setNotes(programEntity.getNotes());
        dto.setPolicy(programEntity.getPolicy());

        // Map associated asset types
        List<AssetTypeDTO> assetTypeDTOs = programEntity.getAssetTypes().stream()
                .map(this::mapAssetTypeToDTO)
                .collect(Collectors.toList());
        dto.setAssetTypes(assetTypeDTOs);

        // You can map other fields as needed

        return dto;
    }


    private AssetTypeDTO mapAssetTypeToDTO(AssetTypeEntity assetTypeEntity) {
        AssetTypeDTO dto = new AssetTypeDTO();
        dto.setId(assetTypeEntity.getId());
        dto.setLevel(assetTypeEntity.getLevel());
        dto.setAssetType(assetTypeEntity.getAssetType());
        dto.setPrice(assetTypeEntity.getPrice());
        dto.setProgramId(assetTypeEntity.getBugBountyProgram().getId());

        return dto;
    }
    @GetMapping("programsById/{id}")
    public ResponseEntity<BugBountyProgramEntity> getBugBountyProgramById(@PathVariable Long id) {
        BugBountyProgramEntity program = programsService.getBugBountyProgramById(id);
        return ResponseEntity.ok(program);
    }


    @GetMapping("/allUsers")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> userEntities = userService.getAllUsers();
        return new ResponseEntity<>(userEntities, HttpStatus.OK);
    }

}
