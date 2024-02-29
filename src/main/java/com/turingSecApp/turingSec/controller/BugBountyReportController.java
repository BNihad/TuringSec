package com.turingSecApp.turingSec.controller;

import com.turingSecApp.turingSec.dao.entities.ReportsEntity;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import com.turingSecApp.turingSec.dao.repository.UserRepository;
import com.turingSecApp.turingSec.service.BugBountyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bug-bounty-reports")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BugBountyReportController {
    @Autowired
    private UserRepository userRepository;

    private final BugBountyReportService bugBountyReportService;

    @Autowired
    public BugBountyReportController(BugBountyReportService bugBountyReportService) {
        this.bugBountyReportService = bugBountyReportService;
    }

    @GetMapping
    public ResponseEntity<List<ReportsEntity>> getAllBugBountyReports() {
        List<ReportsEntity> bugBountyReports = bugBountyReportService.getAllBugBountyReports();
        return new ResponseEntity<>(bugBountyReports, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportsEntity> getBugBountyReportById(@PathVariable Long id) {
        ReportsEntity bugBountyReport = bugBountyReportService.getBugBountyReportById(id);
        return new ResponseEntity<>(bugBountyReport, HttpStatus.OK);
    }


    @PostMapping("/submit")
    public ResponseEntity<?> submitBugBountyReport(@RequestBody ReportsEntity report) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            UserEntity user = userRepository.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            // Set the user for the bug bounty report
            report.setUser(user);

            // Save the bug bounty report
            bugBountyReportService.submitBugBountyReport(report);

            return ResponseEntity.ok("Bug bounty report submitted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportsEntity> updateBugBountyReport(@PathVariable Long id,
                                                                  @RequestBody ReportsEntity bugBountyReport) {
        ReportsEntity updatedReport = bugBountyReportService.updateBugBountyReport(id, bugBountyReport);
        return new ResponseEntity<>(updatedReport, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBugBountyReport(@PathVariable Long id) {
        bugBountyReportService.deleteBugBountyReport(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
