package com.turingSecApp.turingSec.service;

import com.turingSecApp.turingSec.dao.entities.AssetTypeEntity;
import com.turingSecApp.turingSec.dao.entities.BugBountyProgramEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.repository.AssetTypeRepository;
import com.turingSecApp.turingSec.dao.repository.ProgramsRepository;
import com.turingSecApp.turingSec.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramsService {
    @Autowired
    private ProgramsRepository programsRepository;

    public BugBountyProgramEntity createBugBountyProgram(BugBountyProgramEntity program) {

        return programsRepository.save(program);
    }

    public List<BugBountyProgramEntity> getAllBugBountyPrograms() {
        return programsRepository.findAll();
    }

    public BugBountyProgramEntity getBugBountyProgramById(Long id) {
        Optional<BugBountyProgramEntity> program = programsRepository.findById(id);
        return program.orElseThrow(() -> new ResourceNotFoundException("Bug Bounty Program not found"));
    }



    public void deleteBugBountyProgram(Long id) {
        BugBountyProgramEntity program = getBugBountyProgramById(id);
        programsRepository.delete(program);
    }

    // Method to fetch bug bounty programs associated with a specific company
    public List<BugBountyProgramEntity> getAllBugBountyProgramsByCompany(CompanyEntity company) {
        return programsRepository.findByCompany(company);
    }
}
