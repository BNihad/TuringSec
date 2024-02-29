package com.turingSecApp.turingSec.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bug_bounty_programs")
public class BugBountyProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column
    private LocalDate toDate;


    @Column(nullable = false)
    private String assetType;

    @Column(nullable = false)
    private String price;

    @Column
    private String notes;
    @Column
    private String announcement;

    @Column(nullable = false)
    private LocalDate launchDate;

    @Column
    private Long companyId;

    // Getters and setters
}