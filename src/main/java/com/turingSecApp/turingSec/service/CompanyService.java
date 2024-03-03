package com.turingSecApp.turingSec.service;

import com.turingSecApp.turingSec.Response.RegistrationResponse;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.entities.role.Role;
import com.turingSecApp.turingSec.dao.repository.CompanyRepository;
import com.turingSecApp.turingSec.dao.repository.RoleRepository;
import com.turingSecApp.turingSec.exception.EmailAlreadyExistsException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String approveCompanyRegistration(Long companyId) {
        Optional<CompanyEntity> companyOptional = companyRepository.findById(companyId);
        if (companyOptional.isPresent()) {
            CompanyEntity company = companyOptional.get();

            // Generate a random password for the company
            String generatedPassword = generateRandomPassword();
            company.setPassword(passwordEncoder.encode(generatedPassword));

            // Set the approval status to true
            company.setApproved(true);

            // Retrieve the "COMPANY" role
            Role companyRole = roleRepository.findByName("COMPANY");
            if (companyRole == null) {
                throw new NotFoundException("Company role not found.");
            }

            // Save the company
            companyRepository.save(company);

            // Return the generated password
            return generatedPassword;
        } else {
            throw new NotFoundException("Company with the given ID not found.");
        }
    }


    public ResponseEntity<?> registerCompany(CompanyEntity company) {
        // Ensure the company doesn't already exist
        if (companyRepository.findByEmail(company.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Email is already taken.");
        }

        // Set the "COMPANY" role for the company
        Role companyRole = roleRepository.findByName("COMPANY");
        if (companyRole == null) {
            throw new NotFoundException("Company role not found.");
        }
        company.setRoles(Collections.singleton(companyRole));
        company.setApproved(false);

        // Save the company
        companyRepository.save(company);

        // Return the company entity without the generated password
        return ResponseEntity.ok(new RegistrationResponse(company));
    }

    // Other methods for managing companies

    private String generateRandomPassword() {
        // Generate a random alphanumeric password with 12 characters
        return RandomStringUtils.randomAlphanumeric(12);
    }


}
