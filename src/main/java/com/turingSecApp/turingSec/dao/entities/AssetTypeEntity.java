package com.turingSecApp.turingSec.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "asset_types")
public class AssetTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String level;

    private String assetType;

    private String price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bug_bounty_program_id")
    @JsonIgnore
    private BugBountyProgramEntity bugBountyProgram;

    // Getters and setters
}