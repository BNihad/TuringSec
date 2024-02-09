package com.turingSecApp.turingSec.service;

import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public boolean approveCompanyRegistration(Long companyId) {
        Optional<CompanyEntity> companyOptional = companyRepository.findById(companyId);
        if (companyOptional.isPresent()) {
            CompanyEntity company = companyOptional.get();
            company.setApproved(true); // Set the approval status to true
            companyRepository.save(company);
            return true;
        } else {
            return false; // Company with the given ID not found
        }
    }
    
    // Other methods for managing companies
}
