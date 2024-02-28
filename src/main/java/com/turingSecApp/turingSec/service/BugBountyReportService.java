package com.turingSecApp.turingSec.service;

import com.turingSecApp.turingSec.dao.entities.ReportsEntity;
import com.turingSecApp.turingSec.dao.repository.ReportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BugBountyReportService {

    private final ReportsRepository bugBountyReportRepository;

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

    public ReportsEntity createBugBountyReport(ReportsEntity bugBountyReport) {
        return bugBountyReportRepository.save(bugBountyReport);
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
}
