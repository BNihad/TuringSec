package com.turingSecApp.turingSec.dao.repository;

import com.turingSecApp.turingSec.dao.entities.AssetTypeEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import com.turingSecApp.turingSec.dao.entities.HackerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetTypeEntity, Long> {

    List<AssetTypeEntity> findByBugBountyProgram_Company(CompanyEntity company);

}
