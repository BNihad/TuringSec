package com.turingSecApp.turingSec.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bug_bounty_collaborators")
public class CollaboratorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bug_bounty_report_id")
    @JsonIgnore
    private ReportsEntity bugBountyReport;

    @Column(name = "hacker_username")
    private String hackerUsername;

    @Column(name = "collaboration_percentage")
    private Double collaborationPercentage;

    // Constructors, getters, and setters



    // Getters and Setters
}
