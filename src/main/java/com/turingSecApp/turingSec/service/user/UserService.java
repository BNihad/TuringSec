package com.turingSecApp.turingSec.service.user;



import com.turingSecApp.turingSec.dao.entities.hacker.HackerEntity;
import com.turingSecApp.turingSec.dao.entities.role.Role;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import com.turingSecApp.turingSec.dao.repository.HackerRepository;
import com.turingSecApp.turingSec.dao.repository.RoleRepository;
import com.turingSecApp.turingSec.dao.repository.UserRepository;
import com.turingSecApp.turingSec.exception.EmailAlreadyExistsException;
import com.turingSecApp.turingSec.exception.UserAlreadyExistsException;
import com.turingSecApp.turingSec.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HackerRepository hackerRepository;

    @Autowired
    private RoleRepository roleRepository;



    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ResponseEntity<?> registerHacker(UserEntity user) {



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

        sendActivationEmail(user);

        return ResponseEntity.ok(user);
    }

    public boolean activateAccount(String token) {
        // Retrieve user by activation token
        UserEntity user = userRepository.findByActivationToken(token);

        if (user != null && !user.isActivated()) {
            // Activate the user by updating the account status or perform other necessary actions
            user.setActivated(true);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    private void sendActivationEmail(UserEntity user) {
        // Generate activation token and save it to the user entity
        String activationToken = generateActivationToken();
        user.setActivationToken(activationToken);
        userRepository.save(user);

        // Send activation email
        String activationLink = "https://turingsec-production.up.railway.app/api/auth/activate?token=" + activationToken;
        String subject = "Activate Your Account";
        String content = "Dear " + user.getFirst_name() + ",\n\n"
                + "Thank you for registering with our application. Please click the link below to activate your account:\n\n"
                + activationLink + "\n\n"
                + "Best regards,\nThe Application Team";

        emailNotificationService.sendEmail(user.getEmail(), subject, content);
    }

    private String generateActivationToken() {
        // You can implement your own token generation logic here
        // This could involve creating a unique token, saving it in the database,
        // and associating it with the user for verification during activation.
        // For simplicity, you can use a library like java.util.UUID.randomUUID().
        return UUID.randomUUID().toString();
    }

    public UserEntity registerAdmin(UserEntity user) {
        // Ensures the user doesn't exist
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set user roles
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ADMIN")); // Change to ROLE_AUTHOR for authors
        user.setRoles(roles);

        // Save the user
        return userRepository.save(user);
    }
}

