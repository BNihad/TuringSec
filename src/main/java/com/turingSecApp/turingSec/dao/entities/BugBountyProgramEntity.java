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
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String businessTitle;

    @Column(nullable = false)
    private String policyUrl;

    @Column(nullable = false)
    private Integer reportsResolved;

    @Column(nullable = false)
    private String assetsEligible;

    @Column
    private String lowSeverity;

    @Column
    private String mediumSeverity;

    @Column
    private String highSeverity;

    @Column
    private String criticalSeverity;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column
    private LocalDate toDate;

    @Column(nullable = false)
    private String assetsInScope;

    @Column(nullable = false)
    private BigDecimal averageBounty;

    @Column(nullable = false)
    private String assetType;

    @Column(nullable = false)
    private String price;

    @Column
    private String notes;

    @Column(nullable = false)
    private LocalDate launchDate;

    // Getters and setters
}