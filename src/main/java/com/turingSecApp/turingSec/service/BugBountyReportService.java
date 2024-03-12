package com.turingSecApp.turingSec.service;

import com.turingSecApp.turingSec.dao.entities.BugBountyProgramEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.entities.ReportsEntity;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import com.turingSecApp.turingSec.dao.repository.CompanyRepository;
import com.turingSecApp.turingSec.dao.repository.ReportsRepository;
import com.turingSecApp.turingSec.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BugBountyReportService {

    private final ReportsRepository bugBountyReportRepository;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CompanyRepository companyRepository;


    @Autowired
    public BugBountyReportService(ReportsRepository bugBountyReportRepository) {
        this.bugBountyReportRepository = bugBountyReportRepository;
    }

    public List<ReportsEntity> getAllBugBountyReports() {
        return bugBountyReportRepository.findAll();
    }

    public ReportsEntity getBugBountyReportById(Long id) {
        Optional<ReportsEntity> bugBountyReportOptional = bugBountyReportRepository.findById(id);
        return bugBountyReportOptional.orElse(null);
    }

    public void submitBugBountyReport(ReportsEntity report) {
        // You can perform any necessary validation or processing here before saving the report
        bugBountyReportRepository.save(report);
    }

    private String getUsernameFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        throw new RuntimeException("Unable to extract username from JWT token");
    }
    public ReportsEntity updateBugBountyReport(Long id, ReportsEntity updatedReport) {
        Optional<ReportsEntity> bugBountyReportOptional = bugBountyReportRepository.findById(id);
        if (bugBountyReportOptional.isPresent()) {
            ReportsEntity existingReport = bugBountyReportOptional.get();
            // Update existingReport properties with values from updatedReport
            existingReport.setAsset(updatedReport.getAsset());
            existingReport.setWeakness(updatedReport.getWeakness());
            existingReport.setSeverity(updatedReport.getSeverity());
            // Update other properties similarly
            return bugBountyReportRepository.save(existingReport);
        } else {
            return null; // or throw an exception if report not found
        }
    }

    public void deleteBugBountyReport(Long id) {
        bugBountyReportRepository.deleteById(id);
    }



    public List<ReportsEntity> getAllReportsByUser() {
        // Retrieve the username of the authenticated user
        String username = getUsernameFromToken();

        // Find the user by username
        UserEntity user = userRepository.findByUsername(username);

        // If user found, get all reports associated with that user
        if (user != null) {
            return bugBountyReportRepository.findByUser(user);
        } else {
            // If user not found, return an empty list or handle as needed
            return Collections.emptyList();
        }
    }




    public List<ReportsEntity> getBugBountyReportsForCompanyPrograms(CompanyEntity company) {
        // Fetch the company entity along with its bug bounty programs within an active Hibernate session
        company = companyRepository.findById(company.getId()).orElse(null);
        if (company == null) {
            // Handle case where company is not found
            return Collections.emptyList();
        }

        // Access the bug bounty programs
        Set<BugBountyProgramEntity> bugBountyPrograms = company.getBugBountyPrograms();

        // Process the bug bounty programs as needed

        // Return the bug bounty reports
        return bugBountyReportRepository.findByBugBountyProgramIn(bugBountyPrograms);
    }
}
