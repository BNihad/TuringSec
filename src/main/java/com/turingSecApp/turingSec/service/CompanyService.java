package com.turingSecApp.turingSec.service;

import com.turingSecApp.turingSec.Response.RegistrationResponse;
import com.turingSecApp.turingSec.dao.entities.AdminEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.entities.role.Role;
import com.turingSecApp.turingSec.dao.repository.AdminRepository;
import com.turingSecApp.turingSec.dao.repository.CompanyRepository;
import com.turingSecApp.turingSec.dao.repository.RoleRepository;
import com.turingSecApp.turingSec.exception.EmailAlreadyExistsException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.*;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmailNotificationService emailNotificationService;

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
        notifyAdminsForApproval(company);

        // Return the company entity without the generated password
        return ResponseEntity.ok(new RegistrationResponse(company));
    }


    private void notifyAdminsForApproval(CompanyEntity company) {
        // Get a list of administrators from the database or any other source
        List<AdminEntity> admins = adminRepository.findAll(); // Assuming you have an AdminRepository

        // Compose the email message
        String subject = "New Company Registration for Approval";
        String content = "A new company has registered and requires approval.\n\n"
                + "Company Name: " + company.getCompany_name() + "\n"
                + "Contact Person: " + company.getFirst_name() + "\n"
                + "Job Title: " + company.getJob_title() + "\n\n"
                + "Please login to the admin panel to review and approve.";

        // Send email notification to each admin
        for (AdminEntity admin : admins) {
            emailNotificationService.sendEmail(admin.getEmail(), subject, content);
        }
    }



    // Other methods for managing companies

    private String generateRandomPassword() {
        // Generate a random alphanumeric password with 12 characters
        return RandomStringUtils.randomAlphanumeric(12);
    }





}
