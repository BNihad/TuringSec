package com.turingSecApp.turingSec.controller;

import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CompanyController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<CompanyEntity>> getAllCompanies() {
        List<CompanyEntity> companyEntities = userService.getAllCompanies();
        return new ResponseEntity<>(companyEntities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyEntity> getCompaniesById(@PathVariable Long id) {
        CompanyEntity companyEntity = userService.getCompaniesById(id);
        return new ResponseEntity<>(companyEntity, HttpStatus.OK);
    }
}
