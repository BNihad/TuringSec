package com.turingSecApp.turingSec.controller;

import com.turingSecApp.turingSec.dao.entities.ReportsEntity;
import com.turingSecApp.turingSec.service.BugBountyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bug-bounty-reports")
public class BugBountyReportController {

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

    @PostMapping
    public ResponseEntity<ReportsEntity> createBugBountyReport(@RequestBody ReportsEntity bugBountyReport) {
        ReportsEntity createdReport = bugBountyReportService.createBugBountyReport(bugBountyReport);
        return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
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
