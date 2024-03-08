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
import java.util.ArrayList;
import java.util.List;


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

    @Column
    private String notes;

    @Column
    private String policy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private CompanyEntity company;

    @OneToMany(mappedBy = "bugBountyProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssetTypeEntity> assetTypes = new ArrayList<>();

    // Getters and setters
}