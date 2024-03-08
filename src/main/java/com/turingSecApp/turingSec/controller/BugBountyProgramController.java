package com.turingSecApp.turingSec.controller;

import com.turingSecApp.turingSec.Request.AssetTypeDTO;
import com.turingSecApp.turingSec.Request.BugBountyProgramWithAssetTypeDTO;
import com.turingSecApp.turingSec.dao.entities.AssetTypeEntity;
import com.turingSecApp.turingSec.dao.entities.BugBountyProgramEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.repository.AssetTypeRepository;
import com.turingSecApp.turingSec.dao.repository.CompanyRepository;
import com.turingSecApp.turingSec.service.AssetTypeService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bug-bounty-programs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BugBountyProgramController {


    @Autowired
    private ProgramsService bugBountyProgramService;
    @Autowired
    private AssetTypeService assetTypeService;

    @Autowired
    private  CompanyRepository companyRepository;


    @PostMapping
    public ResponseEntity<BugBountyProgramEntity> createBugBountyProgram(@Valid @RequestBody BugBountyProgramWithAssetTypeDTO programDTO) {
        // Get the authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Extract the company from the authenticated user details
        CompanyEntity company = (CompanyEntity) userDetails.getUser();

        // Convert DTO to entity
        BugBountyProgramEntity program = new BugBountyProgramEntity();
        program.setFromDate(programDTO.getFromDate());
        program.setToDate(programDTO.getToDate());
        program.setNotes(programDTO.getNotes());
        program.setPolicy(programDTO.getPolicy());
        program.setCompany(company);

// Convert AssetTypeDTOs to AssetTypeEntities
        List<AssetTypeEntity> assetTypes = programDTO.getAssetTypes().stream()
                .map(assetTypeDTO -> {
                    AssetTypeEntity assetTypeEntity = new AssetTypeEntity();
                    assetTypeEntity.setLevel(assetTypeDTO.getLevel());
                    assetTypeEntity.setAssetType(assetTypeDTO.getAssetType());
                    assetTypeEntity.setPrice(assetTypeDTO.getPrice());
                    assetTypeEntity.setBugBountyProgram(program);
                    return assetTypeEntity;
                })
                .collect(Collectors.toList());

// Set the list of asset types for the program
        program.setAssetTypes(assetTypes);
        // Proceed with creating the bug bounty program
        BugBountyProgramEntity createdProgram = bugBountyProgramService.createBugBountyProgram(program);
        return ResponseEntity.created(URI.create("/api/bug-bounty-programs/" + createdProgram.getId())).body(createdProgram);
    }


    @GetMapping("/assets")
    public ResponseEntity<List<AssetTypeDTO>> getCompanyAssetTypes() {
        // Retrieve the email of the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Retrieve the company associated with the authenticated user
        CompanyEntity company = companyRepository.findByEmail(userEmail);

        // Check if the company is authenticated
        if (company != null) {
            // Get assets belonging to the company
            List<AssetTypeEntity> assetTypeEntities = assetTypeService.getCompanyAssetTypes(company);

            // Map AssetTypeEntities to AssetTypeDTOs if necessary
            List<AssetTypeDTO> assetTypeDTOs = assetTypeEntities.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(assetTypeDTOs);
        } else {
            // Return unauthorized response or handle as needed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    // Method to map AssetTypeEntity to AssetTypeDTO if necessary
    private AssetTypeDTO mapToDTO(AssetTypeEntity assetTypeEntity) {
        AssetTypeDTO assetTypeDTO = new AssetTypeDTO();
        assetTypeDTO.setLevel(assetTypeEntity.getLevel());
        assetTypeDTO.setAssetType(assetTypeEntity.getAssetType());
        assetTypeDTO.setPrice(assetTypeEntity.getPrice());
        return assetTypeDTO;
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