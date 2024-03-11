package com.turingSecApp.turingSec.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssetTypeDTO {
    private Long id;
    private String level;
    private String assetType;
    private String price;
    private Long programId; // New field for program ID

    // Getters and setters
}
