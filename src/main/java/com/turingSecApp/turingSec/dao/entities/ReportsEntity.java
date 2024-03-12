package com.turingSecApp.turingSec.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "bug_bounty_reports")
public class ReportsEntity {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset")
    private String asset;

    @Column(name = "weakness")
    private String weakness;

    @Column(name = "severity")
    private String severity;

    @Column(name = "method_name")
    private String methodName;


    @Column(name = "proof_of_concept")
    private String proofOfConcept;

    @Column(name = "discovery_details")
    private String discoveryDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bug_bounty_program_id")
    @JsonIgnore
    private BugBountyProgramEntity bugBountyProgram;
}
