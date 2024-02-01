package com.turingSecApp.turingSec.user;



import com.turingSecApp.turingSec.dao.entities.hacker.HackerEntity;
import com.turingSecApp.turingSec.dao.entities.role.Role;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import com.turingSecApp.turingSec.dao.repository.HackerRepository;
import com.turingSecApp.turingSec.dao.repository.RoleRepository;
import com.turingSecApp.turingSec.dao.repository.UserRepository;
import com.turingSecApp.turingSec.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HackerRepository hackerRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    PasswordEncoder passwordEncoder;

    public UserEntity registerHacker(UserEntity user) {
        // Ensure the user doesn't exist
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username is already taken.");
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

        return user;
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

