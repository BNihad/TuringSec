package com.turingSecApp.turingSec.service;

import com.turingSecApp.turingSec.dao.entities.AssetTypeEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.repository.AssetTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetTypeService {

    private final AssetTypeRepository assetTypeRepository;

    public AssetTypeService(AssetTypeRepository assetTypeRepository) {
        this.assetTypeRepository = assetTypeRepository;
    }

    public List<AssetTypeEntity> getCompanyAssetTypes(CompanyEntity company) {
        return assetTypeRepository.findByBugBountyProgram_Company(company);
    }
}

