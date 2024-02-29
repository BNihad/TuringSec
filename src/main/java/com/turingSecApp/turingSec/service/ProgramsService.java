package com.turingSecApp.turingSec.service;

import com.turingSecApp.turingSec.dao.entities.BugBountyProgramEntity;
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

    public BugBountyProgramEntity updateBugBountyProgram(Long id, BugBountyProgramEntity updatedProgram) {
        BugBountyProgramEntity existingProgram = getBugBountyProgramById(id);
        existingProgram.setName(updatedProgram.getName());
        existingProgram.setBusinessTitle(updatedProgram.getBusinessTitle());
        existingProgram.setPolicyUrl(updatedProgram.getPolicyUrl());
        existingProgram.setReportsResolved(updatedProgram.getReportsResolved());
        existingProgram.setAssetsEligible(updatedProgram.getAssetsEligible());
        existingProgram.setLowSeverity(updatedProgram.getLowSeverity());
        existingProgram.setMediumSeverity(updatedProgram.getMediumSeverity());
        existingProgram.setHighSeverity(updatedProgram.getHighSeverity());
        existingProgram.setCriticalSeverity(updatedProgram.getCriticalSeverity());
        existingProgram.setFromDate(updatedProgram.getFromDate());
        existingProgram.setToDate(updatedProgram.getToDate());
        existingProgram.setAssetsInScope(updatedProgram.getAssetsInScope());
        existingProgram.setAverageBounty(updatedProgram.getAverageBounty());
        existingProgram.setAssetType(updatedProgram.getAssetType());
        existingProgram.setPrice(updatedProgram.getPrice());
        existingProgram.setNotes(updatedProgram.getNotes());
        existingProgram.setLaunchDate(updatedProgram.getLaunchDate());
        return programsRepository.save(existingProgram);
    }

    public void deleteBugBountyProgram(Long id) {
        BugBountyProgramEntity program = getBugBountyProgramById(id);
        programsRepository.delete(program);
    }
}
