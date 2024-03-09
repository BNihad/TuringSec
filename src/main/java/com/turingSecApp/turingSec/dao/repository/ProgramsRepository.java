package com.turingSecApp.turingSec.dao.repository;

import com.turingSecApp.turingSec.dao.entities.BugBountyProgramEntity;
import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramsRepository extends JpaRepository<BugBountyProgramEntity,Long> {
    List<BugBountyProgramEntity> findByCompany(CompanyEntity company);

}
