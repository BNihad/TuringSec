package com.turingSecApp.turingSec.controller;

import com.turingSecApp.turingSec.dao.entities.BugBountyProgramEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.repository.CompanyRepository;
import com.turingSecApp.turingSec.service.ProgramsService;
import com.turingSecApp.turingSec.service.user.CustomUserDetails;
import com.turingSecApp.turingSec.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bug-bounty-programs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BugBountyProgramController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private ProgramsService bugBountyProgramService;

    @PostMapping
    public ResponseEntity<BugBountyProgramEntity> createBugBountyProgram(@Valid @RequestBody BugBountyProgramEntity program) {
        // Get the authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Extract the company from the authenticated user details
        CompanyEntity company = (CompanyEntity) userDetails.getUser();

        // Set the company for the bug bounty program
        program.setCompany(company);

        // Proceed with creating the bug bounty program
        BugBountyProgramEntity createdProgram = bugBountyProgramService.createBugBountyProgram(program);
        return ResponseEntity.created(URI.create("/api/bug-bounty-programs/" + createdProgram.getId())).body(createdProgram);
    }
    @GetMapping
    @Secured("ROLE_COMPANY")
    public ResponseEntity<List<BugBountyProgramEntity>> getAllBugBountyPrograms() {
        List<BugBountyProgramEntity> programs = bugBountyProgramService.getAllBugBountyPrograms();

        return ResponseEntity.ok(programs);
    }

    @GetMapping("/{id}")
    @Secured("ROLE_COMPANY")
    public ResponseEntity<BugBountyProgramEntity> getBugBountyProgramById(@PathVariable Long id) {
        BugBountyProgramEntity program = bugBountyProgramService.getBugBountyProgramById(id);
        return ResponseEntity.ok(program);
    }


    @DeleteMapping("/{id}")
    @Secured("ROLE_COMPANY")
    public ResponseEntity<Void> deleteBugBountyProgram(@PathVariable Long id) {
        // Get the authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Get the company associated with the authenticated user
        CompanyEntity company = (CompanyEntity) userDetails.getUser();

        // Retrieve the bug bounty program by ID
        BugBountyProgramEntity program = bugBountyProgramService.getBugBountyProgramById(id);

        // Check if the authenticated company is the owner of the program
        if (program.getCompany().getId().equals(company.getId())) {
            // Proceed with deleting the bug bounty program
            bugBountyProgramService.deleteBugBountyProgram(id);
            return ResponseEntity.noContent().build();
        } else {
            // If the authenticated company is not the owner, return forbidden status
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}